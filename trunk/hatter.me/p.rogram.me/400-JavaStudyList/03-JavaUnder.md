Java底层知识

1. 学习了解字节码、class文件格式
    * [!](http://en.wikipedia.org/wiki/Java_class_file)
    * [!](http://en.wikipedia.org/wiki/Java_bytecode)
    * [!](http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings)
1. 写一个程序要求实现javap的功能（手工完成，不借助ASM等工具）
如Java源代码：
%%%% prettify ln=1 rl=1
__public static void main(String[] args) {
____int i = 0;
____i += 1;
____i *= 1;
____System.out.println(i);
__}
%%%
编译后读取class文件输出以下代码：
%%%% prettify ln=1 rl=1
public static void main(java.lang.String[]);
__Code:
___Stack=2, Locals=2, Args_size=1
___0: iconst_0
___1: istore_1
___2: iinc  1, 1
___5: iload_1
___6: iconst_1
___7: imul
___8: istore_1
___9: getstatic #2; //Field java/lang/System.out:Ljava/io/PrintStream;
___12:  iload_1
___13:  invokevirtual #3; //Method java/io/PrintStream.println:(I)V
___16:  return
__LineNumberTable: 
___line 4: 0
___line 5: 2
___line 6: 5
___line 7: 9
___line 8: 16
%%%
1. 使用CGLIB做一个AOP程序
    * [!](http://cglib.sourceforge.net/)
1. 使用ASM实现上述CGLIB实现的功能
    * [!](http://asm.ow2.org/)

#### 相关资料
1. [!](http://book.douban.com/subject/1138768/)
1. [!](http://book.douban.com/subject/6522893/)
1. [!](http://en.wikipedia.org/wiki/Java_class_file)
1. [!](http://en.wikipedia.org/wiki/Java_bytecode)
1. [!](http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings)
1. [!](http://asm.ow2.org/)
1. [!](http://cglib.sourceforge.net/)

