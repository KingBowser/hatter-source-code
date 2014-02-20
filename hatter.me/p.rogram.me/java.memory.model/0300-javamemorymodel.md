!!EXPLAIN
Java内存模型

我们先来看一段代码：

%%% prettify ln=1
final class SetCheck {
  private int  a = 0;
  private long b = 0;

  void set() {
    a =  1;
    b = -1;
  }

  boolean check() {
    return ((b ==  0) ||
            (b == -1 && a == 1)); 
  }
}
%%%

_代码引用自：[!](http://gee.cs.oswego.edu/dl/cpj/jmm.html)_


!!#mainmemandworkmem# 内存模型

<img src="jmm.png"/>


!!#final# final


!!#volatile# volatile

`volatile`关键词是告诉编译器说“这个变量是易变的，请保证始终读、写唯一的值，并且请保证原子性”，当编译器接收到这些信息后，编译器会做如下处理：

1. 禁止编译器本身对该变量的读、写缓存优化
1. 根据当前的微架构，在生成读、写该变量的硬件指令时禁止CPU的高速缓存
    * `X86`：微架构已经保证了缓存的强一致性，无须使用特别的指令
    * `IA64`：使用`ld.acq`或`st.rel`指令进行读、写，保证CPU的高速缓存被更新
1. 禁止对这个变量读写前后的指令重排
    * `X86`：`mfence` or `cpuid` or `locked insn` (`lock; addl $0,0(%%esp)`)
    * `IA64`：`mf`

!!#synchronized# synchronized


!!#contended# @Contended

`@Contended`通过[!JEP 142 (在特定字段上减少高速缓存竞争访问)](http://openjdk.java.net/jeps/142)引入，即针对伪共享提供一种解决方案，解决方法是通过`@Contended`来标记哪些字段是竞争访问的，编译器在编译的时候即将存在竞争访问的字段对齐到不同的缓存线。















