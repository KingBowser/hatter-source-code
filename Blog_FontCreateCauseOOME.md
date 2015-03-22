# Font.create()引发OOME问题 #

周二（2011年12月13日）中午，服务器出现问题，新上的打水印功能出现不可用情况，有大量用户投诉过来。

于是我被从晋升答辩评委席拖回来处理故障。

一看到这个故障我就傻眼了，发现用户打水印时好时坏，于是我自己也做了下测试，发现程序功能没有问题，但还是陆续有用户报告故障上来。

跟进问题发现，服务器 “`/tmp`” 文件夹下大量生成 “`+~JF343ABD3434343342323232.tmp`” 的临时文件。

我的第一个反映就是这是什么呀，拉下来看看。

打开文件，看到几个字符 “方正大黑” ，这不是我们新加的字体么？

看了下程序源代码：
```
InputStream inputSteam = ClassName.class.getResourceAsStream("/resource/FONT_NAME.TTF");
Font font = Font.createFont(Font.TRUE_TYPE_FONT, inputStream);
```

查看方法 “`java.awt.Font.createFont(ILjava.io.InputStrem;)Ljava.awt.Font;`” 的源代码，看到这行代码就恍然大悟了：

return File.createTempFile("+~JF", ".tmp", null);

原来通过流创建字体时会创建临时文件，而且这个临时文件有2.1M之大，打了上千个水印临时文件夹就耗尽了，所以这时通过脚本不断去删除临时文件夹的文件，然后准备紧急发布将File.createFont创建对象改为静态（static）来解决这个问题。

但是到下午3点多有服务器Load飙高到100多，这时候重启应用并加入新机器后开始恢复正常。

服务器Load飙高非常不正常，想了好久都没想出来为什么，后来查看监控的信息，看到内存信息非常诡异：

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/blog/Mem_Graph.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/blog/Mem_Graph.png)

看到这个图严重怀疑是Java的Native Memory内存泄漏，因为按照我们的配置，Java正常内存使用一般不会超过2.5G，而这里我们机器的内存使用了超过5G，而且还使用了Swap内存，所以这时也导致Load飙高。但为什么Java会使用这么多内存呢？

然后我从Sun(Oracle)的Bug Database中找到了这个Bug：

http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7074159

上面的状态是“3-Accepted, bug”，说明的确是个BUG并且已经被确认。

从描述看和我们遇到的问题基本一样，然后把代码拿下来测试，惊奇的发现不会导致OOM。

这时果断放弃使用Mac来测试，将程序搞到我的Linux虚拟机上跑，这时内存果然涨到1G以上，OOM再现。

通过如下测试程序，可以还原Java Native OOM问题：
```
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

publicclass TestTrueTypeFontLoad {

  publicstaticvoid main(String args[]) {
    try {
      for(int i = 0; i < 50000; i++) {
        Font afont = Font.createFont(Font.PLAIN, new File("arial.ttf"));
        BufferedImage bimg = new BufferedImage(100, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = bimg.createGraphics();
        gs.setFont(afont.deriveFont(Font.PLAIN, new AffineTransform(10, 0, 0, 10, 10, 10)));
        gs.drawString("This is font load test", 0, 0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  } 
}
```

通过把openjdk-6中的源代码拿过来编译，然后在运行代码前加上配置 “-Dsun.boot.class.path=” 加上刚才编译出来的类 ，再通过命令 “java -verbose:class” 打印出加载的类，看到JVM加载的类的确是我编译的类。然后通过不断修改源文件打印日志并监控Java占用内存情况。

终于排查出来，`sun.font.Font2D`会将FontStrike对象缓存到内存中，默认使用SoftReference在ConcurrentHashMap中保存对其的引用（可以通过`sun.java2d.font.reftype`来配置使用SoftReference或WeakReference，默认则使用SoftReference，在类`sun.font.StrikeCache`中），而FontStrike则包含了对Native Memory内存的引用，而通过下面的函数来释放这些Native Memory：
```
sun.font.StrikeCache.disposeStrike(sun.font.FontStrikeDisposer)V
```

于是果断在这个函数内加上System.out.println日志，但没有看到这个函数被调用，觉得非常奇怪，这个对象怎么没有被释放呢，而且他Hold的可是Native Memory啊。

于是，将`sun.font.Font2D`的 `getStrike(Lsun.font.FontStrikeDesc;J)Lsun.font.FontStrike;` 函数中的 “`lastFontStrike = new SoftReference(strike);`” 修改为 “`lastFontStrike = new WeakReference(strike);`”；在启动参数上加上 “`-Dsun.java2d.font.reftype=weak`”，且在程序中加入“`System.gc();`” 触发JVM的FULL GC，再次观察内存情况，发现程序内存稳定在500M左右，到此故障排查结束。

关于Reference，大家可以Google一下“`WeakReference vs SoftReference`”。

可以看到WeakReference是在做Full GC时会回收，但SoftReference只在OOME前回收内存。

Java内存和JVM Native内存的关系如下图表示：

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/blog/System_Java_Memory.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/blog/System_Java_Memory.png)