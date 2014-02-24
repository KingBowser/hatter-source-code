!!EXPLAIN
背景

虽然计算机的发展历史也就短短的几十年，但计算机计算的发展却非常迅速，特别是现在已经出现了几十核的`CPU`，但芯片的时钟频率目前已是非常难以提升，芯片厂商也只有发展多核这条路了。对于程序员来说，如何在多核情况下充分利用好硬件资源写出能够“正确”的代码就显的尤为重要。

Java内存模型就是教人如果在并发编程的情况下写出“正确”的代码。导致程序员可能写出“不正确”的代码的因素很多，如编译器的优化、CPU高速缓存优化等。这些优化有些是软件实现的，有些是硬件实现的，而Java内存模型是通过一个抽象模型（用来屏蔽软件或硬件的具体实现）指导程序员写出并发“正确”的代码。


!!#somanymicroarch# 种类繁多的微架构

%%% wikiTable class=table___table-bordered style=width:600px;
|| 微架构 || 设计 || 说明 ||
|| X86 | `CISC` | `IA32 ➟ AMD64 ➟ Intel64` |
|| IA64 | `EPIC` | `Intel` `HP` |
|| ARM | `RISC` | `Advanced RISC Machine` |
|| Power PC | `RISC` | `Apple–IBM–Motorola alliance` |
|| ALPHA | `RISC` | ... |
|| PA-RISC | `RISC` | ... |
|*4 ... |
%%%

_EPIC相关介绍: [!Itanium 处理器系列的 EPIC 架构](ia64/HP_Integrity_document_02.pdf)_


!!#multithread# 多线程(并行)编程

对于传统的单核处理器来说微架构层面不存在可见性的问题，因为因为CPU仅拥有一套高速缓存，不同线程不可能因此读到不同值。

并发和并行的区别就是一个处理器同时处理多个任务和多个处理器或者是多核的处理器同时处理多个不同的任务。前者是逻辑上的同时发生（simultaneous），而后者是物理上的同时发生：
* 并发性(concurrency)，又称共行性，是指能处理多个同时性活动的能力，并发事件之间不一定要同一时刻发生。
* 并行(parallelism)是指同时发生的两个并发事件，具有并发的含义，而并发则不一定并行。

_来个比喻：并发和并行的区别就是一个人同时吃三个馒头和三个人同时吃三个馒头。_

<img src="concurrentparalel.jpg"/>

<br>
_图片引用自：[!](http://www.cnblogs.com/NickyYe/archive/2008/12/01/1344802.html)_

线程间并行运行的问题举例会在“微架构优化举例”中提到。


!!#compiler# 编译器优化举例

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



!!#micromarch# 微架构优化举例

`x86`和`x64`(`x86_64`)实现了强一致的内存模型，即所有的内存访问都已经是`volatile`的。所以`volatile`的字段会强制编译器避免做了些高级优化，如在一个循环中读写一个变量，生成的代码将会是一个非`volatile`的内存读（这种方式已经在上面举例说明）。

然而，安腾处理器实现了一个弱的内存模型。对于目标处理器如果是安腾的话，对于`volatile`内存访问编译器需要使用一些特别的指令：`ld.acq`和`st.red`替代`ld`和`st`。指令`ld.acq`的意思是说“请先刷新缓存并读入该值”，而指令`st.rel`是说“把值写入缓存，并将这个值刷新到主内存”。而`ld`和`st`指令仅访问处理器的缓存，而这个缓存对于别的处理器可能是不可见的。

下面我们看一下图例：

<br>
#### 1. 初始状态

<img src="/java.memory.model/ia64/mm/1-init.png"/>

<br>
#### 2. 普通写

对于非`volatile`的写数据有可能是仅仅更新的当前线程所在处理器的缓存，而未更新该值到主内存中：

<img src="/java.memory.model/ia64/mm/2-write.png"/>

<br>
#### 3. `volatile`写

对于`volatile`写数据将会先写入当前线程所有处理器的缓存，然后即将对应的缓存数据刷新到主内存中。如，我们将字段`v`设置为`11`，那么变量值`u`、`v`都会被刷新到主内存中：

<img src="/java.memory.model/ia64/mm/3-volatile-write.png"/>

<br>
#### 4. 普通读

通常来说，对于非`volatile`读则仅从当前线程所在处理器的缓存读入值，而不是从主内存中。所以，即便线程1将`u`设置为`11`，而线程2在读`u`时，他也将只能读到`10`。

<img src="/java.memory.model/ia64/mm/4-read.png"/>

<br>
#### 5. `volatile`读

最后，我们看一个`volatile`读的例子。线程2通过`volatile`读到字段`v`的最新值：

<img src="/java.memory.model/ia64/mm/5-volatile-read.png"/>

<br>
_图片引用自：[!](http://igoro.com/archive/volatile-keyword-in-c-memory-model-explained/)_

