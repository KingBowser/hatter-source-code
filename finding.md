# Introduction #

Java programmer's grep. Finding can grep in '.classpath', jar/war/zip files and normal source files, and output the result in color.

# Details #

```
wget https://hatter-source-code.googlecode.com/svn/trunk/finding/findingall.jar
```

Usage:
```
$ java -jar findingall.jar ---NCL Pattern

OR

$ alias finding='java -jar /ABSTRACT_PATH_OF_findingall.jar -o UTF-8 ---NCL'
$ finding Pattern
```

Arguments description:
```
$ finding 
Usage:
  Version: 1.0 (b20130428)
  java -jar findingall.jar [options] <text>
    -f <option>                  option(default [c,h,cpp,hpp,java,xml])
       ALL                       all file(s)
       c                         .c file(s)
       h                         .h file(s)
       cpp                       .cpp file(s)
       hpp                       .hpp file(s)
       java                      .java file(s)
    -I <file>                    file name(s) from input file
    -o <charset>                 console output charset
    -r <charset[,charset]>       input charset(s), deafult is: UTF-8,GB18030
    -has <symbol>                only the line has symbol(case insensitive, -HAS case sensitive)
    -ff <filter>                 file and path filter(regex, starts with '~' means exclude)
    -CC <thread count>           concurrent thread(s) count
    -cs <option>                 color schema
        c1                       schema 1
        c2                       schema 2
    --i                          ignore case contains
    --E                          regex
    --e                          ignore case regex
    --0                          only print file name
    --1                          one file matches only one time
    --s                          print simple file name
    --F                          print full file name
    --N                          print match at new line
    --C                          color print
    --L                          line number print
    --J                          match jar and zip file(s)
    --c                          match .class file(s)
```

Outputs:<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/finding/finding.png' />

Find in jar with argument '--J':<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/finding/finding-in-jar-and-ignorecase.png' />

Through '.classpath' file and find in jar and chinese output:<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/finding/finding-in-jar-and-chinese.png' />

Shell offen accept UTF-8 ouput, so you can add this argument:<br>
<pre><code>-o UTF-8<br>
</code></pre>

Find all java.lang.Long.intValue() and int intValue = (int) longValue:<br>
<pre><code>finding --J --c -ff ".jar$" -ff '.class$' 'java/lang/Long.intValue'<br>
<br>
finding --J --c -ff ".jar$" -ff '.class$' 'L2I'<br>
</code></pre>