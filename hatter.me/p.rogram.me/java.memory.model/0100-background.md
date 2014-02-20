!!EXPLAIN
背景

虽然计算机的发展历史也就短短的几十年，但计算机计算的发展却非常迅速，特别是现在已经出现了几十核的`CPU`，但芯片的时钟频率目前已是非常难以提升，芯片厂商也只有发展多核这条路了。对于程序员来说，如何在多核情况下充分利用好硬件资源写出能够“正确”的代码就显的尤为重要。

Java内存模型就是教人如果在并发编程的情况下写出“正确”的代码。导致程序员可能写出“不正确”的代码的因素很多，如编译器的优化、CPU高速缓存优化等。这些优化有些是软件实现的，有些是硬件实现的，而Java内存模型是通过一个抽象模型（用来屏蔽软件或硬件的具体实现）指导程序员写出并发“正确”的代码。


!!#somanymicroarch# 种类繁多的微架构

%%% wikiTable class=table___table-bordered style=width:600px;
|| 微架构 || 设计 || 说明 ||
|| X86 | `CISC` | `IA32`,`AMD64`,`Intel64` |
|| IA64 | `EPIC` |  |
|| ARM | `RISC` |  |
|| Power PC | `RISC` |  |
|| ALPHA | `RISC` |  |
|| PA-RISC | `RISC` |  |
|*4 ... |
%%%

_EPIC相关介绍: [!Itanium 处理器系列的 EPIC 架构](ia64/HP_Integrity_document_02.pdf)_


!!#multithread# 多线程(并发)编程


!!#compiler# 编译器的各种优化

下面是一个`C#`的关于`volatile`的例子：

%%% prettify ln=1
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
%%%

<span class="glyphicon glyphicon-eye-open" style="color:blue;"></span>请看以下循环代码：
```
while (test1._loop == true) ;
```

<span class="glyphicon glyphicon-exclamation-sign" style="color:red;"></span> 但有可能实现上运行如下的代码：
```
if (test1._loop) { while (true); }
```

<span class="glyphicon glyphicon-exclamation-sign" style="color:red;"></span> 即等同于编译后的循环代码汇编如下：
```
00000068  test        eax,eax 
0000006a  jne         00000068
```

<span class="glyphicon glyphicon-ok" style="color:green;"></span> 如果将`_loop`变量标记为`volatile`，那么生成的代码如下：
```
00000064  cmp         byte ptr [eax+4],0 
00000068  jne         00000064
```

从上面的例子我们可以看到当一个变量是否标记为`volatile`会影响编译器生成的代码是否访问“缓存”(寄存器)，因为访问寄存器快于L1，且远远快于内存访问。

<br>
_代码引用自：[!](http://igoro.com/archive/volatile-keyword-in-c-memory-model-explained/)_



!!#micromarch# 微架构的各种优化

The mainstream x86 and x64 processors implement a strong memory model where memory access is effectively volatile. So, a volatile field forces the compiler to avoid some high-level optimizations like hoisting a read out of a loop, but otherwise results in the same assembly code as a non-volatile read.

The Itanium processor implements a weaker memory model. To target Itanium, the JIT compiler has to use special instructions for volatile memory accesses: LD.ACQ and ST.REL, instead of LD and ST. Instruction LD.ACQ effectively says, “refresh my cache and then read a value” and ST.REL says, “write a value to my cache and then flush the cache to main memory”. LD and ST on the other hand may just access the processor’s cache, which is not visible to other processors.

<br><br>
----
#### 1. 初始状态

<img src="/java.memory.model/ia64/mm/1-init.png"/>

<br><br>
----
#### 2. 普通写

A non-volatile write could just update the value in the thread’s cache, and not the value in main memory:

<img src="/java.memory.model/ia64/mm/2-write.png"/>

<br><br>
----
#### 3. `volatile`写

However, in C# all writes are volatile (unlike say in Java), regardless of whether you write to a volatile or a non-volatile field. So, the above situation actually never happens in C#.

A volatile write updates the thread’s cache, and then flushes the entire cache to main memory. If we were to now set the volatile field v to 11, both values u and v would get flushed to main memory:

<img src="/java.memory.model/ia64/mm/3-volatile-write.png"/>

<br><br>
----
#### 4. 普通读

Since all C# writes are volatile, you can think of all writes as going straight to main memory.

A regular, non-volatile read can read the value from the thread’s cache, rather than from main memory. Despite the fact that thread 1 set u to 11, when thread 2 reads u, it will still see value 10:

<img src="/java.memory.model/ia64/mm/4-read.png"/>

<br><br>
----
#### 5. `volatile`读

When you read a non-volatile field in C#, a non-volatile read occurs, and you may see a stale value from the thread’s cache. Or, you may see the updated value. Whether you see the old or the new value depends on your compiler and your processor.

Finally, let’s take a look at an example of a volatile read. Thread 2 will read the volatile field v:

<img src="/java.memory.model/ia64/mm/5-volatile-read.png"/>

<br>
_图片引用自：[!](http://igoro.com/archive/volatile-keyword-in-c-memory-model-explained/)_

