# Introduction #

Show class stat in perm


# Details #

```
wget https://hatter-source-code.googlecode.com/svn/trunk/javatools/classlist/classlistall.jar
```

```
Usage:
  java -jar classlistall.jar [options] <PID>
    -show <class name>       filter by full class name
    -grouping <firt n>       group class first n package
    --detail                 print detail
    --short                  print short class name
```

Sample:
```
$java -jar classlistall.jar 1808 -grouping 2
[INFO] Add system classloader jar url: /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/sa-jdi.jar
Attaching to process ID 1808, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 20.6-b01-415
......
Total class count: 659
Total class size: 3929840 (H: 3.75M)
Average total class size: 5963
==== Grouping depth: 2 ====
Grouping Package                    Count     Size         Human(size)
----------------------------------------------------------------------
java.util                           126        798760       780.04K
java.lang                           145        749184       731.62K
sun.security                        107        736288       719.03K
java.security                       68         305064       297.91K
java.io                             41         265864       259.63K
sun.misc                            45         254760       248.79K
java.nio                            17         168640       164.69K
sun.nio                             22         117376       114.62K
sun.reflect                         23         96456        94.20K
java.math                           4          81856        79.94K
java.net                            11         78264        76.43K
sun.util                            9          73320        71.60K
sun.net                             7          59848        58.45K
sun.jkernel                         3          53528        52.27K
sun.text                            8          44840        43.79K
com.apple                           8          25648        25.05K
javax.security                      1          7552         7.38K
java.text                           3          5232         5.11K
T                                   1          1520         1.48K
```