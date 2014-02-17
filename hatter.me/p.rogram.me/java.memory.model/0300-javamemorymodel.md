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


!!#final# final


!!#volatile# volatile



!!#synchronized# synchronized




