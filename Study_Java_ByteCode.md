## 1. Class格式及字节码介绍 ##
Class文件的格式如下 <sup>[6]</sup>：
```
struct Class_File_Format {
   u4 magic_number;   // HEX:CAFEBABE
   u2 minor_version;   
   u2 major_version;   
 
   u2 constant_pool_count; 
   cp_info constant_pool[constant_pool_count - 1];
 
   u2 access_flags;
   u2 this_class;
   u2 super_class;
 
   u2 interfaces_count;
   u2 interfaces[interfaces_count];
 
   u2 fields_count;   
   field_info fields[fields_count];
 
   u2 methods_count;
   method_info methods[methods_count];
 
   u2 attributes_count;   
   attribute_info attributes[attributes_count];
}
```
详细说明请参看：[Class格式](http://en.wikipedia.org/wiki/Java_class_file)

[字节码指令介绍](http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings)

那么，字节码是如何执行的？<br>
先看一下下面一段Java代码：<br>
<pre><code>int i = 1;<br>
int j = 3;<br>
int sum = i + j;<br>
System.out.println(sum);<br>
</code></pre>

通过<code>javac</code>编译，并<code>javap -v</code>反编译以上代码：<br>
<pre><code>Code:<br>
   Stack=2, Locals=4, Args_size=1<br>
   0:     iconst_1<br>
   1:     istore_1<br>
   2:     iconst_3<br>
   3:     istore_2<br>
   4:     iload_1<br>
   5:     iload_2<br>
   6:     iadd<br>
   7:     istore_3<br>
   8:     getstatic     #2; //Field java/lang/System.out:Ljava/io/PrintStream;<br>
   11:     iload_3<br>
   12:     invokevirtual     #3; //Method java/io/PrintStream.println:(I)V<br>
   15:     return<br>
  LineNumberTable:<br>
   line 4: 0<br>
   line 5: 2<br>
   line 6: 4<br>
   line 7: 8<br>
   line 8: 15<br>
</code></pre>

直接打印出class文件的相关内容为：<br>
<pre><code>04 3c 06 3d 1b 1c 60 3e b2 00 02 1d b6 00 03 b1<br>
</code></pre>

对以上class内容反编译 <sup>[5]</sup>如下：<br>
<pre><code>04  iconst_1<br>
3c  istore_1<br>
06  iconst_3<br>
3d  istore_2<br>
1b  iload_1<br>
1c  iload_2<br>
60  iadd<br>
3e  istore_3<br>
b2  getstatic [ constant pool index = index1 &lt;&lt; 8 + index2, here is "2" ]<br>
00      index1<br>
02      index2<br>
1d  iload_3<br>
b6  invokevirtual [ constant pool index = index1 &lt;&lt; 8 + index2, here is "3" ]<br>
00      index1<br>
03      index2<br>
b1  return<br>
</code></pre>

JVM中执行bytecode是在栈上执行的，以上字节码是这样运行的：<br>
<img src='https://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/bytecode_interpreter.png' />

<h2>2. 字节码操纵框架</h2>
<table><thead><th> <code>ASM</code> </th><th> <a href='http://asm.ow2.org/'>http://asm.ow2.org/</a> </th><th> ObjectWeb ASM轻量级的Java字节码处理框架。它可以动态生成二进制格式的stub类或其他代理类，或者在类被JAVA虚拟机装入内存之前，动态修改类。ASM 提供了与 BCEL和SERP相似的功能，只有22K的大小，比起350K的BCEL和150K的SERP来说，是相当小巧的，并且它有更高的执行效率，是BCEL的7倍，SERP的11倍以上。 <sup>[1]</sup> </th></thead><tbody>
<tr><td> <code>BCEL</code> </td><td> <a href='http://commons.apache.org/bcel/'>http://commons.apache.org/bcel/</a> </td><td> Byte Code Engineering Library (BCEL)，这是Apache Software Foundation 的Jakarta 项目的一部分。BCEL是 Java classworking 最广泛使用的一种框架,它可以让您深入 JVM 汇编语言进行类操作的细节。BCEL与Javassist 有不同的处理字节码方法，BCEL在实际的JVM 指令层次上进行操作(BCEL拥有丰富的JVM 指令级支持)而Javassist 所强调的源代码级别的工作。 <sup>[1]</sup> </td></tr>
<tr><td> <code>Javassist</code> </td><td> <a href='http://www.javassist.org/'>http://www.javassist.org/</a> </td><td> Javassist是一个开源的分析、编辑和创建Java字节码的类库。是由东京技术学院的数学和计算机科学系的 Shigeru Chiba 所创建的。它已加入了开放源代码JBoss 应用服务器项目,通过使用Javassist对字节码操作为JBoss实现动态AOP框架。 <sup>[1]</sup> </td></tr>
<tr><td> <code>cglib</code> </td><td> <a href='http://cglib.sourceforge.net/'>http://cglib.sourceforge.net/</a> </td><td> cglib是一个强大的,高性能,高质量的Code生成类库。它可以在运行期扩展Java类与实现Java接口。Hibernate用它来实现PO字节码的动态生成。 <sup>[1]</sup> </td></tr>
<tr><td> <code>jclasslib</code> </td><td> <a href='http://www.ej-technologies.com/products/jclasslib/overview.html'>http://www.ej-technologies.com/products/jclasslib/overview.html</a> </td><td> JClassLib不但是一个字节码阅读器而且还包含一个类库允许开发者读取,修改,写入Java Class文件与字节码。 <sup>[1]</sup> </td></tr></tbody></table>

<h2>3. 反编译软件</h2>
<table><thead><th> <code>jd</code> </th><th> <a href='http://java.decompiler.free.fr/'>http://java.decompiler.free.fr/</a> </th><th> JD分为JD-GUI、JD-Eclipse两种运行方式，JD-GUI是以单独的程序的方式运行，JD-Eclipse则是以一个Eclipse插件的方式运行。 <sup>[2]</sup> </th></thead><tbody>
<tr><td> <code>jad</code> </td><td> <a href='http://www.varaneckas.com/jad/'>http://www.varaneckas.com/jad/</a> </td><td> jad 是一个使用非常广泛的 Java 反编译工具。 <sup>[3]</sup> </td></tr></tbody></table>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.open-open.com/54.htm'>http://www.open-open.com/54.htm</a><br>
<code>[2].</code> <a href='http://baike.baidu.com/view/1872199.htm'>http://baike.baidu.com/view/1872199.htm</a><br>
<code>[3].</code> <a href='http://www.oschina.net/p/jad'>http://www.oschina.net/p/jad</a><br>
<code>[4].</code> <a href='http://aprilsoft.cn/blog/post/308.html'>http://aprilsoft.cn/blog/post/308.html</a><br>
<code>[5].</code> <a href='http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings'>http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings</a><br>
<code>[6].</code> <a href='http://en.wikipedia.org/wiki/Java_class_file'>http://en.wikipedia.org/wiki/Java_class_file</a><br>