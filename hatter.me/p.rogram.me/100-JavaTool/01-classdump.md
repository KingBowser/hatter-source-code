classdump

Get class dump:
```
wget https://hatter-source-code.googlecode.com/svn/trunk/classdump/classdumpall.jar
```

Usage:
```
  java -jar classdumpall.jar [options] <PID>
    -filter <class name regex>       filter by classname
    -output <dir>                    output directory
    --i                              ignore case
    --color                          color output
    --hidejar                        hide from jar
    --showclassloaders               show url classloaders
```

