## 安装 ##
  1. 下载二进制文件，或源代码自行编译：https://kenai.com/projects/base-hsdis/downloads
  1. 在Linux上将文件 linux-hsdis-amd64.so 改名为 libhsdis-amd64.so
  1. 复制文件到目录： $JAVA\_HOME/jre/lib/amd64/server

## 使用 ##
通过下面命令：
```
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly InterThreadLatency
```
或
```
java -XX:+UnlockDiagnosticVMOptions '-XX:CompileCommand=print,*PongRunner.run' InterThreadLatency
```

## Examining generated code ##
The following HotSpot options (with an -XX: prefix on the command line) require OpenJDK 7 and an externally loadable disassembler plugin:

  * `+PrintAssembly` print assembly code for bytecoded and native methods
  * `+PrintNMethods` print nmethods as they are generated
  * `+PrintNativeNMethods` print native method wrappers as they are generated
  * `+PrintSignatureHandlers` print native method signature handlers
  * `+PrintAdapterHandlers` print adapters (i2c, c2i) as they are generated
  * `+PrintStubCode` print stubs: deopt, uncommon trap, exception, safepoint, runtime support
  * `+PrintInterpreter` print interpreter code

These flags are "diagnostic", meaning that they must be preceded by `-XX:+UnlockDiagnosticVMOptions`. On the command line, they must all be preceded by -XX:. They may also be placed in a flags file, .hotspotrc by default, or configurable as `-XX:Flags=myhotspotrc.txt`.

The disassembly output is annotated with various kinds of debugging information, such as field names and source locations. The quality of this information improved markedly in January 2010 (bug fix 6912062).

## Filtering Output ##
The -XX:+PrintAssembly option prints everything. If that's too much, drop it and use one of the following options.

Individual methods may be printed:

`CompileCommand=print,*MyClass.myMethod` prints assembly for just one method
`CompileCommand=option,*MyClass.myMethod,PrintOptoAssembly` (debug build only) produces the old print command output
`CompileCommand=option,*MyClass.myMethod,PrintNMethods` produces method dumps
These options accumulate.

If you get no output, use `-XX:+PrintCompilation` to verify that your method is getting compiled at all.

## Reading the compiler's mind ##
The `-XX:+LogCompilation` flag produces a low-level XML file about compiler and runtime decisions, which may be interesting to some. The `-XX:+UnlockDiagnosticVMOptions` must come first. The dump is to hotspot.log in the current directory; use `-XX:LogFile=foo.log` to change this.

The LogCompilation output is basic line-oriented XML. It can usefully be read in a text editor, and there are also tools for parsing and scanning it.

### 参考资料 ###
`[1].` http://mechanical-sympathy.blogspot.com/2013/06/printing-generated-assembly-code-from.html<br>
<code>[2].</code> <a href='https://wikis.oracle.com/display/HotSpotInternals/PrintAssembly'>https://wikis.oracle.com/display/HotSpotInternals/PrintAssembly</a><br>
<code>[3].</code> <a href='https://kenai.com/projects/base-hsdis'>https://kenai.com/projects/base-hsdis</a><br>