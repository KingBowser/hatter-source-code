# Introduction #

Dump class in HotSpot JVM.

# Details #

Get class dump:
```
wget https://hatter-source-code.googlecode.com/svn/trunk/javatools/classdump/classdumpall.jar
```

```
Usage:
  java -jar classdumpall.jar [options] <PID>
    -filter <class name regex>       filter by classname
    -output <dir>                    output directory
    --i                              ignore case
    --color                          color output
    --hidejar                        hide from jar
    --showclassloaders               show url classloaders
```

Sample:
```
$ java -jar classdumpall.jar 1808 -filter ^com -output _dump
[INFO] Add system classloader jar url: /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/sa-jdi.jar
Attaching to process ID 1808, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 20.6-b01-415
[INFO] Dump class: com.apple.java.AppleSystemLog
[INFO] Dump class: com.apple.java.Application
[INFO] Dump class: com.apple.java.Usage$2
[INFO] Dump class: com.apple.java.Usage
[INFO] Dump class: com.apple.java.BackwardsCompatibility
[INFO] Dump class: com.apple.java.Usage$3
[INFO] Dump class: com.apple.java.Application$1
[INFO] Dump class: com.apple.java.Usage$1
```

Output classes:
```
Hatter-Jiangs-MacBook-Pro:_dump hatterjiang$ htree 
_dump
`-- com
    `-- apple
        `-- java
            |-- AppleSystemLog.class
            |-- Application$1.class
            |-- Application.class
            |-- BackwardsCompatibility.class
            |-- Usage$1.class
            |-- Usage$2.class
            |-- Usage$3.class
            `-- Usage.class
```

```
$ java -jar classdumpall.jar --showclassloaders --color 1252
```

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/classdump/classdump_showclassloaders.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/classdump/classdump_showclassloaders.png)