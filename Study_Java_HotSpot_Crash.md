# `hs_err_pid*.log`文件格式 #

Crash文件默认在当前路径下生成，也可以通过参数指定：
```
java -XX:ErrorFile=/var/log/java/java_error%p.log
```

产生该文件可能的错误：
  1. The operating exception or signal that provoked the fatal error
  1. Version and configuration information
  1. Details on the thread that provoked the fatal error and thread's stack trace
  1. The list of running threads and their state
  1. Summary information about the heap
  1. The list of native libraries loaded
  1. Command line arguments
  1. Environment variables
  1. Details about the operating system and CPU

文件头格式：
```
#
# An unexpected error has been detected by Java Runtime Environment:
#
#  SIGSEGV (0xb) at pc=0x417789d7, pid=21139, tid=1024
#
# Java VM: Java HotSpot(TM) Client VM (1.6.0-rc-b63 mixed mode, sharing)
# Problematic frame:
# C  [libNativeSEGV.so+0x9d7]

#
# If you would like to submit a bug report, please visit:
#   http://java.sun.com/webapps/bugreport/crash.jsp
#

This example shows that the VM crashed on an unexpected signal. The next line describes the signal type, program counter
 (pc) that caused the signal, process ID and thread ID, as follows.

#  SIGSEGV (0xb) at pc=0x417789d7, pid=21139, tid=1024
      |      |           |             |         +--- thread id
      |      |           |             +------------- process id
      |      |           +--------------------------- program counter
      |      |                                        (instruction pointer)
      |      +--------------------------------------- signal number
      +---------------------------------------------- signal name
The next line contains the VM version (Client VM or Server VM), an indication whether the application was run in mixed
 or interpreted mode, and an indication whether class file sharing was enabled.

# Java VM: Java HotSpot(TM) Client VM (1.6.0-rc-b63 mixed mode, sharing)
The next information is the function frame that caused the crash, as follows.

# Problematic frame:
# C  [libNativeSEGV.so+0x9d7]
  |              +-- Same as pc, but represented as library name and offset.
  |                  For position-independent libraries (JVM and most shared
  |                  libraries), it is possible to inspect the instructions
  |                  that caused the crash without a debugger or core file
  |                  by using a disassembler to dump instructions near the
  |                  offset.
  +----------------- Frame type
In this example, the “C” frame type indicates a native C frame. The following table shows the possible frame types.
```

错误信号详见：[Study\_OS\_Linux\_Signal](Study_OS_Linux_Signal.md)

| `C`  |本地C帧 |
|:-----|:----------|
| `j` | 解释的Java帧 |
| `V` | 虚拟机帧 |
| `v` | 虚拟机生成的存根栈帧 |
| `J` | 其他帧类型，包括编译后的Java帧 |

线程信息：
```
Current thread (0x0805ac88):  JavaThread "main" [_thread_in_native, id=21139]
                    |             |         |            |          +-- ID
                    |             |         |            +------------- state
                    |             |         +-------------------------- name
                    |             +------------------------------------ type
                    +-------------------------------------------------- pointer
```

# 对指令反编译 #
```
wget http://hatter-source-code.googlecode.com/svn/trunk/hserranalysis/hserranalysisall.jar
```

```
$ java -jar hserranalysisall.jar x86disassemble
[INFO] Add system classloader jar url: /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/sa-jdi.jar
X86DisassemblerAnalysiser :
> 0x417789c7:   ec 14 e8 72 ff ff ff 81 c3 f2 13 00 00 8b 45 08
 0x417789c7:  inb	%al
 0x417789c8:  adcb	%al, 0xe8
 0x417789ca:  jb	0x417789cb
 0x417789cc:  bad opcode
 0x417789cd:  incl	1307331[%ecx]
 0x417789d3:  addb	2117[%ebx], %cl
> 0x417789d7:   0f b6 00 88 45 fb 8d 83 6f ee ff ff 89 04 24 e8
 0x417789d7:  movzbl	%eax, [%eax]
 0x417789da:  movb	-5[%ebp], %al
 0x417789dd:  leal	%eax, -4497[%ebx]
 0x417789e3:  movl	[%esp], %eax
 0x417789e6:  call	0x417789eb
> exit
Exit!
```

# Core dump #
```
ulimit -c unlimited

$ file core.12989 
core.12989: ELF 64-bit LSB core file AMD x86-64, version 1 (SYSV), SVR4-style, from 'jconsole'
```

```
gdb -c core.xxxx <exec>

info threads
bt/where
bt full
disassemble
```

### 参考资料 ###
`[1].` http://rednaxelafx.iteye.com/blog/729214<br>
<code>[2].</code> <a href='http://hllvm.group.iteye.com/group/topic/34848'>http://hllvm.group.iteye.com/group/topic/34848</a><br>
<code>[3].</code> <a href='http://www.raychase.net/1459'>http://www.raychase.net/1459</a><br>
<code>[4].</code> <a href='http://www.oracle.com/technetwork/java/javase/felog-138657.html'>http://www.oracle.com/technetwork/java/javase/felog-138657.html</a><br>