jtop:::jtop - JVM的top命令

jtop is for 'JVM TOP'.

```
wget https://hatter-source-code.googlecode.com/svn/trunk/jtop/jtop.jar
```

Usage:
```
$ java -jar jtop.jar 
[ERROR] pid is not assigned.
Usage[b121209]:
java -jar jtop.jar [options] <pid> [<interval> [<count>]]
-OR-
java -cp jtop.jar jtop [options] <pid> [<interval> [<count>]]
    -size <B|K|M|G|H>             Size, case insensitive (default: B, H for human)
    -thread <N>                   Thread Top N (default: 5)
    -stack <N>                    Stacktrace Top N (default: 8)
    -excludes                     Excludes (string.contains)
    -includes                     Includes (string.contains, excludes than includes)
    --color                       Display color (default: off)
    --sortmem                     Sort by memory allocted (default: off)
    --summaryoff                  Do not display summary (default: off)
    --advanced                    Do display like 'top' (default: off)
```


