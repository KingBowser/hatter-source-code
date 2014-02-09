其他知识

1. 学习使用git
    * [!](https://github.com/)
    * [!](https://git.oschina.net/)
1. 学习使用gradle
    * [!](http://www.gradle.org/)
1. 学习一个小语种语言
    * Groovy
    * Scala
    * LISP, Common LISP, Schema, Clojure
    * R
    * Julia
    * Lua
    * Ruby
    * ...
1. 尝试了解编码的本质
    * 了解以下概念
        * ASCII, ISO-8859-1
        * GB2312, GBK, GB18030
        * Unicode, UTF-8
    * 不使用 String.getBytes() 等其他工具类/函数完成下面功能
%%%% prettify ln=1 rl=1
____public static void main(String[] args) throws IOException {
________String str = "Hello, 我们是中国人。";
________byte[] utf8Bytes = toUTF8Bytes(str);
________FileOutputStream fos = new FileOutputStream("f.txt");
________fos.write(utf8Bytes);
________fos.close();
____}
____
____public static byte[] toUTF8Bytes(String str) {
________return null; // TODO
____}
%%%%
    * 想一下上面的程序能不能写一个转GBK的？
    * 写个程序自动判断一个文件是哪种编码
1. 尝试了解时间的本质
    * 了解以下概念
        * 时区 & 冬令时、夏令时
            * [!](http://en.wikipedia.org/wiki/Time_zone)
            * [!](ftp://ftp.iana.org/tz/data/asia)
            * [!](http://zh.wikipedia.org/wiki/%E4%B8%AD%E5%9C%8B%E6%99%82%E5%8D%80)
        * 闰年
            * [!](http://en.wikipedia.org/wiki/Leap_year)
        * 闰秒
            * [!](ftp://ftp.iana.org/tz/data/leapseconds)
    * `System.currentTimeMillis()`返回的时间是什么

相关资料
1. [!](http://git-scm.com/)
1. [!](http://en.wikipedia.org/wiki/UTF-8)
1. [!](http://www.iana.org/time-zones)

