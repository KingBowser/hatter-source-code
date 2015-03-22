![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/java.jpeg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/java.jpeg)

[Code Conventions](http://www.oracle.com/technetwork/java/codeconventions-150003.pdfJava)      [2](http://javascript.crockford.com/javacodeconventions.pdf)

http://www.javapractices.com/

# JVM (Java Virtual Machine) #

| `Sun Classic ` |
|:---------------|
| `Exact VM` |
| `HotSpot` |
| `JRockit` |
| `J9` |
| `Harmony` |
| `Azul VM` |
| `Zulu` |
| `BEA Liquid VM` |

http://rednaxelafx.iteye.com/blog/362738

Java Bytecode Instruction: http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings

JVM note book: https://code.google.com/p/jvmnotebook/

GC: http://www.cs.kent.ac.uk/people/staff/rej/gc.html

```
find . -name '*.java' -print > java.list
javac -d classes @java.list

find . -name '*.class' -print > classes.list
jar cf my.jar @classes.list
```

```
jstat -J-Djstat.showUnsupported=true -snap <pid>
```

```
-XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation
>>> hotspot.log
```

http://jinwoohwang.com/index.html

虚拟JUG：http://www.youtube.com/user/virtualJUG?feature=watch

## Java Performance ##
  * http://www.javaperformancetuning.com/

  1. Java不慢
  1. 反射不慢
  1. 异常不慢

## Java Concurrent ##
  * http://sourceforge.net/projects/javaconcurrenta/
  * http://gee.cs.oswego.edu/dl/concurrency-interest/jsr166-slides.pdf

## 参考书籍 ##
| 《深入Java虚拟机(原书第2版)》 | http://book.douban.com/subject/1138768/ |
|:-----------------------------------------|:----------------------------------------|
| 《深入理解Java虚拟机》 | http://book.douban.com/subject/6522893/ |
| 《Effective Java 第二版 中文版》 | http://book.douban.com/subject/3360807/ |


### 参考资料 ###
`[1].` http://en.wikipedia.org/wiki/Java_(programming_language)<br>
<code>[2].</code> <a href='http://en.wikipedia.org/wiki/Java_virtual_machine'>http://en.wikipedia.org/wiki/Java_virtual_machine</a><br>
<code>[3].</code> <a href='http://en.wikipedia.org/wiki/List_of_Java_virtual_machines'>http://en.wikipedia.org/wiki/List_of_Java_virtual_machines</a><br>
<code>[4].</code> <a href='http://blog.csdn.net/qiucaijuan/article/details/6672981'>http://blog.csdn.net/qiucaijuan/article/details/6672981</a><br>
<code>[5].</code> <a href='https://code.google.com/p/jvmnotebook/'>https://code.google.com/p/jvmnotebook/</a><br>
<code>[6].</code> <a href='http://www.cs.kent.ac.uk/people/staff/rej/gc.html'>http://www.cs.kent.ac.uk/people/staff/rej/gc.html</a><br>