通过 `kill -l` 查看信号量，如：
```
# kill -l
 1) SIGHUP       2) SIGINT       3) SIGQUIT      4) SIGILL
 5) SIGTRAP      6) SIGABRT      7) SIGBUS       8) SIGFPE
 9) SIGKILL     10) SIGUSR1     11) SIGSEGV     12) SIGUSR2
13) SIGPIPE     14) SIGALRM     15) SIGTERM     17) SIGCHLD
18) SIGCONT     19) SIGSTOP     20) SIGTSTP     21) SIGTTIN
22) SIGTTOU     23) SIGURG      24) SIGXCPU     25) SIGXFSZ
26) SIGVTALRM   27) SIGPROF     28) SIGWINCH    29) SIGIO
30) SIGPWR      31) SIGSYS      34) SIGRTMIN    35) SIGRTMIN+1
36) SIGRTMIN+2  37) SIGRTMIN+3  38) SIGRTMIN+4  39) SIGRTMIN+5
40) SIGRTMIN+6  41) SIGRTMIN+7  42) SIGRTMIN+8  43) SIGRTMIN+9
44) SIGRTMIN+10 45) SIGRTMIN+11 46) SIGRTMIN+12 47) SIGRTMIN+13
48) SIGRTMIN+14 49) SIGRTMIN+15 50) SIGRTMAX-14 51) SIGRTMAX-13
52) SIGRTMAX-12 53) SIGRTMAX-11 54) SIGRTMAX-10 55) SIGRTMAX-9
56) SIGRTMAX-8  57) SIGRTMAX-7  58) SIGRTMAX-6  59) SIGRTMAX-5
60) SIGRTMAX-4  61) SIGRTMAX-3  62) SIGRTMAX-2  63) SIGRTMAX-1
64) SIGRTMAX
```

`1) SIGHUP`<br>
本信号在用户终端连接(正常或非正常)结束时发出, 通常是在终端的控制进程结束时, 通知同一session内的各个作业, 这时它们与控制终端不再关联。<br>
登录Linux时，系统会分配给登录用户一个终端(Session)。在这个终端运行的所有程序，包括前台进程组和后台进程组，一般都属于这个 Session。当用户退出Linux登录时，前台进程组和后台有对终端输出的进程将会收到SIGHUP信号。这个信号的默认操作为终止进程，因此前台进 程组和后台有终端输出的进程就会中止。不过可以捕获这个信号，比如wget能捕获SIGHUP信号，并忽略它，这样就算退出了Linux登录，wget也 能继续下载。<br>
<br>
此外，对于与终端脱离关系的守护进程，这个信号用于通知它重新读取配置文件。<br>
<br>
<code>2) SIGINT</code><br>
程序终止(interrupt)信号, 在用户键入INTR字符(通常是Ctrl-C)时发出，用于通知前台进程组终止进程。<br>
<br>
<code>3) SIGQUIT</code><br>
和SIGINT类似, 但由QUIT字符(通常是Ctrl-\)来控制. 进程在因收到SIGQUIT退出时会产生core文件, 在这个意义上类似于一个程序错误信号。<br>
<br>
<code>4) SIGILL</code><br>
执行了非法指令. 通常是因为可执行文件本身出现错误, 或者试图执行数据段. 堆栈溢出时也有可能产生这个信号。<br>
<table><thead><th> ILL_ILLOPC </th><th> 非法opcode </th></thead><tbody>
<tr><td> ILL_ILLOPN </td><td> 非法操作数 </td></tr>
<tr><td> ILL_ADR </td><td> 非法寻址模式 </td></tr>
<tr><td> ILL_ILLTRP </td><td> 非法陷井 </td></tr>
<tr><td> ILL_PRVOPC </td><td> 特许的opcode </td></tr>
<tr><td> ILL_PRVREG </td><td> 特许的记数器 </td></tr>
<tr><td> ILL_COPROC </td><td> coprocessor 错误 </td></tr>
<tr><td> ILL_BADSTK </td><td> 内部堆错误 </td></tr></tbody></table>

<code>5) SIGTRAP</code><br>
由断点指令或其它trap指令产生. 由debugger使用。<br>
<br>
<code>6) SIGABRT</code><br>
调用abort函数生成的信号。<br>
<br>
<code>7) SIGBUS</code><br>
非法地址, 包括内存地址对齐(alignment)出错。比如访问一个四个字长的整数, 但其地址不是4的倍数。它与SIGSEGV的区别在于后者是由于对合法存储地址的非法访问触发的(如访问不属于自己存储空间或只读存储空间)。<br>
<br>
<code>8) SIGFPE</code><br>
在发生致命的算术运算错误时发出. 不仅包括浮点运算错误, 还包括溢出及除数为0等其它所有的算术的错误。<br>
<br>
<code>9) SIGKILL</code><br>
用来立即结束程序的运行. 本信号不能被阻塞、处理和忽略。如果管理员发现某个进程终止不了，可尝试发送这个信号。<br>
<br>
<code>10) SIGUSR1</code><br>
留给用户使用<br>
<br>
<code>11) SIGSEGV</code><br>
试图访问未分配给自己的内存, 或试图往没有写权限的内存地址写数据.<br>
<br>
信号 11，即表示程序中可能存在特定条件下的非法内存访问。<br>
<br>
<code>12) SIGUSR2</code><br>
留给用户使用<br>
<br>
<code>13) SIGPIPE</code><br>
管道破裂。这个信号通常在进程间通信产生，比如采用FIFO(管道)通信的两个进程，读管道没打开或者意外终止就往管道写，写进程会收到SIGPIPE信号。此外用Socket通信的两个进程，写进程在写Socket的时候，读进程已经终止。<br>
<br>
<code>14) SIGALRM</code><br>
时钟定时信号, 计算的是实际的时间或时钟时间. alarm函数使用该信号.<br>
<br>
<code>15) SIGTERM</code><br>
程序结束(terminate)信号, 与SIGKILL不同的是该信号可以被阻塞和处理。通常用来要求程序自己正常退出，shell命令kill缺省产生这个信号。如果进程终止不了，我们才会尝试SIGKILL。<br>
<br>
<code>17) SIGCHLD</code><br>
子进程结束时, 父进程会收到这个信号。<br>
<br>
如果父进程没有处理这个信号，也没有等待(wait)子进程，子进程虽然终止，但是还会在内核进程表中占有表项，这时的子进程称为僵尸进程。这种情 况我们应该避免(父进程或者忽略SIGCHILD信号，或者捕捉它，或者wait它派生的子进程，或者父进程先终止，这时子进程的终止自动由init进程 来接管)。<br>
<br>
<code>18) SIGCONT</code><br>
让一个停止(stopped)的进程继续执行. 本信号不能被阻塞. 可以用一个handler来让程序在由stopped状态变为继续执行时完成特定的工作. 例如, 重新显示提示符<br>
<br>
<code>19) SIGSTOP</code><br>
停止(stopped)进程的执行. 注意它和terminate以及interrupt的区别:该进程还未结束, 只是暂停执行. 本信号不能被阻塞, 处理或忽略.<br>
<br>
<code>20) SIGTSTP</code><br>
停止进程的运行, 但该信号可以被处理和忽略. 用户键入SUSP字符时(通常是Ctrl-Z)发出这个信号<br>
<br>
<code>21) SIGTTIN</code><br>
当后台作业要从用户终端读数据时, 该作业中的所有进程会收到SIGTTIN信号. 缺省时这些进程会停止执行.<br>
<br>
<code>22) SIGTTOU</code><br>
类似于SIGTTIN, 但在写终端(或修改终端模式)时收到.<br>
<br>
<code>23) SIGURG</code><br>
有”紧急”数据或out-of-band数据到达socket时产生.<br>
<br>
<code>24) SIGXCPU</code><br>
超过CPU时间资源限制. 这个限制可以由getrlimit/setrlimit来读取/改变。<br>
<br>
<code>25) SIGXFSZ</code><br>
当进程企图扩大文件以至于超过文件大小资源限制。<br>
<br>
<code>26) SIGVTALRM</code><br>
虚拟时钟信号. 类似于SIGALRM, 但是计算的是该进程占用的CPU时间.<br>
<br>
<code>27) SIGPROF</code><br>
类似于SIGALRM/SIGVTALRM, 但包括该进程用的CPU时间以及系统调用的时间.<br>
<br>
<code>28) SIGWINCH</code><br>
窗口大小改变时发出.<br>
<br>
<code>29) SIGIO</code><br>
文件描述符准备就绪, 可以开始进行输入/输出操作.<br>
<br>
<code>30) SIGPWR</code><br>
Power failure<br>
<br>
<code>31) SIGSYS</code><br>
非法的系统调用。<br>
<br>
<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.dbafree.net/?p=870'>http://www.dbafree.net/?p=870</a><br>