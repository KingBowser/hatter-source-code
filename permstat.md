Print string instance count and size in perm.

Code copied from OpenJDK's `jmap -permstat`, and remove the other codes, so published under LGPL.

```
wget https://hatter-source-code.googlecode.com/svn/trunk/javatools/permstat/permstatall.jar
```

Usage:
```
Usage:
  java -jar permstatall.jar [options] <PID> [<interval> [<count>]]
    -size B|K|M|G|H              Byte/KB/MB/GB/Humarn(default B)
```

Sample:
```
$ java -jar permstatall.jar 1022 -size h 1000
[INFO] Add system classloader jar url: /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/sa-jdi.jar
Attaching to process ID 1022, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 20.6-b01-415
COUNT       SIZE                AVERAGE(byte)       DIFF COUNT  DIFF SIZE           
691399      42.26M              64                  -           -                   
932381      56.96M              64                  240982      14.71M  
```