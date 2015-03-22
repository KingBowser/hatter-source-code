# CLR内存模型 #
<table width='100%'><tr><td align='right'>
Microsoft's CLI implementation(CLR)'s memory model<br>
</td></tr></table>

What is a memory model?  It’s the abstraction that makes the reality of today’s exotic hardware comprehensible to software developers.

The reality of hardware is that CPUs are renaming registers, performing speculative and out-of-order execution, and fixing up the world during retirement.  Memory state is cached at various levels in the system (L0 thru L3 on modern X86 boxes, presumably with more levels on the way).  Some levels of cache are shared between particular CPUs but not others.  For example, L0 is typically per-CPU but a hyper-threaded CPU may share L0 between the logical CPUs of a single physical CPU.  Or an 8-way box may split the system into two hemispheres with cache controllers performing an elaborate coherency protocol between these separate hemispheres.  If you consider caching effects, at some level all MP (multi-processor) computers are NUMA (non-uniform memory access).  But there’s enough magic going on that even a Unisys 32-way can generally be considered as UMA by developers.

## 一、编译器优化 ##

请看示例: <sup>[7]</sup>
```
class Test
{
    private bool _loop = true;

    public static void Main()
    {
        Test test1 = new Test();

        // Set _loop to false on another thread
        new Thread(() => { test1._loop = false;}).Start();

        // Poll the _loop field until it is set to false
        while (test1._loop == true) ;

        // The loop above will never terminate!
    }
}
```

编译出来的代码将会这样：
```
00000068  test        eax,eax
0000006a  jne         00000068
```
由于 `_loop` 未加 `volatile` 关键词，所以编译在优化时读取了在 `EAX` 寄存中的值，即在单线程情况下运行是没有问题的。

如果将 `_loop` 标记为 `volatile` 时，编译出来的代码如下：
```
00000064  cmp         byte ptr [eax+4],0
00000068  jne         00000064
```
即程序生成的代码每次都会从主内存读取需要的数据(关于是否能读到真实的值还会在处理器优化中讨论到)。

## 二、处理器优化 ##
在多核系统下，不同的处理核拥有不同的缓存。在默认情况下，这些处理核不一定每次都保持所有的缓存行一致，需要一些特别的指令将缓存行刷到内存中。

## 三、深入理解volatile读/写 ##

在多处理器中，每个核都有各自的缓存(如L0, L1, L2)，具体参看 [Study\_Java\_HotSpot\_Concurrent](Study_Java_HotSpot_Concurrent.md) 。假设程序读写两个变量 `u, v`。<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/dotnet/csharp/csharp_volatile_0.png' />

当程序非 <code>volatile</code> 写内存时：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/dotnet/csharp/csharp_volatile_1.png' />

然而在 <code>C#</code> 中，所有的写数据都是 <code>volatile</code> 的，所以在 <code>C#</code> 中写变量则如下：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/dotnet/csharp/csharp_volatile_2.png' />

在读非 <code>volatile</code> 的变量时如下(没有读到内存中实际的值，取得到的是缓存过已经过期的值)：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/dotnet/csharp/csharp_volatile_3.png' />

当读取 <code>volatile</code> 的值的时候，则变成：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/dotnet/csharp/csharp_volatile_4.png' />

<h2>四、内存模型和.Net操作</h2>

<table><thead><th> <b>Construct</b> </th><th> <b>Refreshes thread cache before?</b> </th><th> <b>Flushes thread cache after?</b> </th><th> <b>Notes</b> </th></thead><tbody>
<tr><td> Ordinary read </td><td> No </td><td> No </td><td> Read of a non-volatile field </td></tr>
<tr><td> Ordinary write </td><td> No </td><td> <b>Yes</b> </td><td> Write of a non-volatile field </td></tr>
<tr><td> Volatile read </td><td> <b>Yes</b> </td><td> No </td><td> Read of volatile field, or Thread.VolatileRead </td></tr>
<tr><td> Volatile write </td><td> No </td><td> <b>Yes</b> </td><td> Write of a volatile field – same as non-volatile </td></tr>
<tr><td> Thread.MemoryBarrier </td><td> <b>Yes</b> </td><td> <b>Yes</b> </td><td> Special memory barrier method </td></tr>
<tr><td> Interlocked operations </td><td> <b>Yes</b> </td><td> <b>Yes</b> </td><td> Increment, Add, Exchange, etc. </td></tr>
<tr><td> Lock acquire </td><td> <b>Yes</b> </td><td> No </td><td> Monitor.Enter or entering a lock {}  region </td></tr>
<tr><td> Lock release </td><td> No </td><td> <b>Yes</b> </td><td> Monitor.Exit or exiting a lock {} region </td></tr></tbody></table>



<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-335.pdf'>http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-335.pdf</a><br>
<code>[2].</code> <a href='http://blogs.msdn.com/b/jaredpar/archive/2008/01/17/clr-memory-model.aspx'>http://blogs.msdn.com/b/jaredpar/archive/2008/01/17/clr-memory-model.aspx</a><br>
<code>[3].</code> <a href='http://www.bluebytesoftware.com/blog/2008/06/13/VolatileReadsAndWritesAndTimeliness.aspx'>http://www.bluebytesoftware.com/blog/2008/06/13/VolatileReadsAndWritesAndTimeliness.aspx</a><br>
<code>[4].</code> <a href='http://blogs.msdn.com/b/cbrumme/archive/2003/05/17/51445.aspx'>http://blogs.msdn.com/b/cbrumme/archive/2003/05/17/51445.aspx</a><br>
<code>[5].</code> <a href='http://www.bluebytesoftware.com/blog/2007/11/10/CLR20MemoryModel.aspx'>http://www.bluebytesoftware.com/blog/2007/11/10/CLR20MemoryModel.aspx</a><br>
<code>[6].</code> <a href='http://msdn.microsoft.com/en-us/magazine/cc163715.aspx'>http://msdn.microsoft.com/en-us/magazine/cc163715.aspx</a><br>
<code>[7].</code> <a href='http://igoro.com/archive/volatile-keyword-in-c-memory-model-explained/'>http://igoro.com/archive/volatile-keyword-in-c-memory-model-explained/</a><br>