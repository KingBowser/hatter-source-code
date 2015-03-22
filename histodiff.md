# Introduction #

Histo-Diff.


# Details #

We can use `jmap -histo` get object count in heap, but we don't know the increase objects between two `jmap -histo`, And the `histodiff` is help you do this.

Usage:
```
wget https://hatter-source-code.googlecode.com/svn/trunk/javatools/histoana/histodiffall.jar
```
```
Usage:
  histodiff [flags] <jpid> [<interval> [<count>]]
  java -jar histodiffall.jar [flags] <jpid> [<interval> [<count>]]
    -top <count>    Top class count[default 10, -1 unlimit]
    --ordercount    Order by count[default by size]
    --live          Dump live objects[default off]
    <jpid>          Java PID
    <interval>      Print interval(ms)[default 5000ms]
    <count>         Print count[default unlimit]
Sample:
  histodiff          -- display this message
  histodiff 12345    -- print jpid 12345's histo info
```

Command:
```
java -jar histodiffall.jar  388 5000 3
```
Outputs:
```
[INFO] Attach to vm: 388
---- histo diff ----
   num  #instances      #bytes    #human  #aver  class name
    0:      101693     3254176     3.10M     32  java.lang.String
    1:       90722     2727568     2.60M     30  [C

---- histo diff ----
   num  #instances      #bytes    #human  #aver  class name
    0:      298821    11155984    10.64M     37  [C
    1:      199214     6374848     6.08M     32  java.lang.String
    2:       99607     2390568     2.28M     24  java.lang.StringBuilder
    3:          41       41600    40.62K   1014  [I

---- histo diff ----
   num  #instances      #bytes    #human  #aver  class name
    0:       56016     1792512     1.71M     32  java.lang.String
    1:       21179      120336   117.52K      5  [C

[INFO] Detach from vm.
```