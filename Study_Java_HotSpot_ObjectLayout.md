## Java对象的内存结构 <sup>[1]</sup> ##

#### 规则1：所有的对象都按8字节对齐 ####
#### 规则2：类属性按如下规则排序： ####
> `1. doubles and longs`<br>
<blockquote><code>2. ints and floats</code><br>
<code>3. shorts and chars</code><br>
<code>4. booleans and bytes</code><br>
<code>5. references</code><br>
对象属性按各自间隙对齐，即按4字节对齐，通常情况下如果按4字节对象，那么读入4个字节到寄存器时性能要高的多<br>
<h4>规则3：属于不同类的类属性不会混合在一起，顺序是先父类（按规则2排序），接下来是子类</h4>
<h4>规则4：父类的最后一个属性和子类的第一个属性按4字节对齐</h4>
<h4>规则5：如果子类的第一个属性是long或double，和父类无法按8字节对齐，那么JVM会按int，short，byte和reference的顺序将一个属性放到子类前直到空隙被填满</h4></blockquote>

<br><br>

<h3>示例1：</h3>
<pre><code>class MyClass {<br>
    byte a;<br>
    int c;<br>
    boolean d;<br>
    long e;<br>
    Object f;        <br>
}<br>
</code></pre>
<code>&gt;&gt;&gt;&gt;</code>
<pre><code>[HEADER:  8 bytes]  8<br>
[e:       8 bytes] 16<br>
[c:       4 bytes] 20<br>
[a:       1 byte ] 21<br>
[d:       1 byte ] 22<br>
[padding: 2 bytes] 24<br>
[f:       4 bytes] 28<br>
[padding: 4 bytes] 32<br>
</code></pre>

<h3>示例2：</h3>
<pre><code>class A {<br>
   long a;<br>
   int b;<br>
   int c;<br>
}<br>
<br>
class B extends A {<br>
   long d;<br>
}<br>
</code></pre>
<code>&gt;&gt;&gt;&gt;</code>
<pre><code>[HEADER:  8 bytes]  8<br>
[a:       8 bytes] 16<br>
[b:       4 bytes] 20<br>
[c:       4 bytes] 24<br>
[d:       8 bytes] 32<br>
</code></pre>

<h3>示例3：</h3>
<pre><code>class A {<br>
   byte a;<br>
}<br>
<br>
class B {<br>
   byte b;<br>
}<br>
</code></pre>
<code>&gt;&gt;&gt;&gt;</code>
<pre><code>[HEADER:  8 bytes]  8<br>
[a:       1 byte ]  9<br>
[padding: 3 bytes] 12<br>
[b:       1 byte ] 13<br>
[padding: 3 bytes] 16<br>
</code></pre>

<h3>示例4：</h3>
<pre><code>class A {<br>
  byte a;<br>
}<br>
<br>
class B {<br>
  long b;<br>
  short c;  <br>
  byte d;<br>
}<br>
</code></pre>
<code>&gt;&gt;&gt;&gt;</code>
<pre><code>[HEADER:  8 bytes]  8<br>
[a:       1 byte ]  9<br>
[padding: 3 bytes] 12<br>
[c:       2 bytes] 14<br>
[d:       1 byte ] 15<br>
[padding: 1 byte ] 16<br>
[b:       8 bytes] 24<br>
</code></pre>

<h3>示例5：</h3>
<pre><code>new boolean[3]<br>
</code></pre>
<code>&gt;&gt;&gt;&gt;</code>
<pre><code>[HEADER:  12 bytes] 12<br>
[[0]:      1 byte ] 13<br>
[[1]:      1 byte ] 14<br>
[[2]:      1 byte ] 15<br>
[padding:  1 byte ] 16<br>
</code></pre>

<h3>示例6：</h3>
<pre><code>new long[3]<br>
</code></pre>
<code>&gt;&gt;&gt;&gt;</code>
<pre><code>[HEADER:  12 bytes] 12<br>
[padding:  4 bytes] 16<br>
[[0]:      8 bytes] 24<br>
[[1]:      8 bytes] 32<br>
[[2]:      8 bytes] 40<br>
</code></pre>

<br><br>
<code>MemoryLayoutSpecification</code> <sup>[2]</sup>:<br>
<pre><code>  /**<br>
   * Describes constant memory overheads for various constructs in a JVM implementation.<br>
   */<br>
  public interface MemoryLayoutSpecification {<br>
<br>
    /**<br>
     * Returns the fixed overhead of an array of any type or length in this JVM.<br>
     *<br>
     * @return the fixed overhead of an array.<br>
     */<br>
    int getArrayHeaderSize();<br>
<br>
    /**<br>
     * Returns the fixed overhead of for any {@link Object} subclass in this JVM.<br>
     *<br>
     * @return the fixed overhead of any object.<br>
     */<br>
    int getObjectHeaderSize();<br>
<br>
    /**<br>
     * Returns the quantum field size for a field owned by an object in this JVM.<br>
     *<br>
     * @return the quantum field size for an object.<br>
     */<br>
    int getObjectPadding();<br>
<br>
    /**<br>
     * Returns the fixed size of an object reference in this JVM.<br>
     *<br>
     * @return the size of all object references.<br>
     */<br>
    int getReferenceSize();<br>
<br>
    /**<br>
     * Returns the quantum field size for a field owned by one of an object's ancestor superclasses<br>
     * in this JVM.<br>
     *<br>
     * @return the quantum field size for a superclass field.<br>
     */<br>
    int getSuperclassFieldPadding();<br>
  }<br>
</code></pre>

32位：<br>
<pre><code>new MemoryLayoutSpecification() {<br>
    @Override public int getArrayHeaderSize() {<br>
        return 12;<br>
    }<br>
    @Override public int getObjectHeaderSize() {<br>
        return 8;<br>
    }<br>
    @Override public int getObjectPadding() {<br>
        return 8;<br>
    }<br>
    @Override public int getReferenceSize() {<br>
        return 4;<br>
    }<br>
    @Override public int getSuperclassFieldPadding() {<br>
        return 4;<br>
    }<br>
}<br>
</code></pre>

64位压缩指针：<br>
<pre><code>new MemoryLayoutSpecification() {<br>
    @Override public int getArrayHeaderSize() {<br>
        return 16;<br>
    }<br>
    @Override public int getObjectHeaderSize() {<br>
        return 12;<br>
    }<br>
    @Override public int getObjectPadding() {<br>
        return 8;<br>
    }<br>
    @Override public int getReferenceSize() {<br>
        return 4;<br>
    }<br>
    @Override public int getSuperclassFieldPadding() {<br>
        return 4;<br>
    }<br>
}<br>
</code></pre>

64位：<br>
<pre><code>new MemoryLayoutSpecification() {<br>
    @Override public int getArrayHeaderSize() {<br>
        return 24;<br>
    }<br>
    @Override public int getObjectHeaderSize() {<br>
        return 16;<br>
    }<br>
    @Override public int getObjectPadding() {<br>
        return 8;<br>
    }<br>
    @Override public int getReferenceSize() {<br>
        return 8;<br>
    }<br>
    @Override public int getSuperclassFieldPadding() {<br>
        return 8;<br>
    }<br>
}<br>
</code></pre>


<br>

<h3>参数资料</h3>
<code>[1].</code> <a href='http://www.codeinstructions.com/2008/12/java-objects-memory-structure.html'>http://www.codeinstructions.com/2008/12/java-objects-memory-structure.html</a><br>
<code>[2].</code> <a href='https://github.com/twitter/commons/blob/master/src/java/com/twitter/common/objectsize/ObjectSizeCalculator.java'>https://github.com/twitter/commons/blob/master/src/java/com/twitter/common/objectsize/ObjectSizeCalculator.java</a><br>