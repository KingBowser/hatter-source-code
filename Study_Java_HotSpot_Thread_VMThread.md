
```
class VM_Operation: public CHeapObj
```

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/vm_operation_queues.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/vm_operation_queues.png)

#### safepoint介绍 <sup>[1]</sup> ####
safe point 顾明思意，就是安全点，当需要jvm做一些操作的时候，需要把当前正在运行的线程进入一个安全点的状态（也可以说停止状态），这样才能做一些安全的操作，比如线程的dump，堆栈的信息。

在jvm里面通常vm\_thread（我们一直在谈论的做一些属于vm 份内事情的线程） 和cms\_thread（内存回收的线程）做的操作，是需要将其他的线程通过调用`SafepointSynchronize::begin` 和 `SafepointSynchronize:end`来实现让其他的线程进入或者退出safe point 的状态。

#### 通常safepoint的有三种状态 <sup>[1]</sup> ####
| `_not_synchronized` | 说明没有任何打断现在所有线程运行的操作，也就是vm thread, cms thread 没有接到操作的指令 |
|:--------------------|:-----------------------------------------------------------------------------------------------------------------------|
| `_synchronizing` | vm thread,cms thread 接到操作指令，正在等待所有线程进入safe point |
| `_synchronized` | 所有线程进入safe point, vm thread, cms thread 可以开始指令操作 |

### Java线程的状态 <sup>[1]</sup> ###
通常在java 进程中的Java 的线程有几个不同的状态，如何让这些线程进入safepoint 的状态中，jvm是采用不同的方式

#### a. 正在解释执行 ####
由于java是解释性语言，而线程在解释java 字节码的时候，需要dispatch table,记录方法地址进行跳转的，那么这样让线程进入停止状态就比较容易了，只要替换掉dispatch table 就可以了，让线程知道当前进入softpoint 状态。<br>
java里会设置3个DispatchTable，<code>_active_table，  _normal_table， _safept_table</code>
<table><thead><th> <code>_active_table</code> </th><th> 正在解释运行的线程使用的dispatch table </th></thead><tbody>
<tr><td> <code>_normal_table</code> </td><td> 就是正常运行的初始化的dispatch table </td></tr>
<tr><td> <code>_safept_table</code> </td><td> safe point需要的dispatch table </td></tr></tbody></table>

解释运行的线程一直都在使用<code>_active_table</code>,关键处就是在进入saftpoint 的时候，用<code>_safept_table</code>替换<code>_active_table</code>, 在退出saftpoint 的时候，使用<code>_normal_table</code>来替换<code>_active_table</code>。<br>
Source Code:<br>
<pre><code>void TemplateInterpreter::notice_safepoints() {  <br>
  if (!_notice_safepoints) {  <br>
    // switch to safepoint dispatch table  <br>
    _notice_safepoints = true;  <br>
    copy_table((address*)&amp;_safept_table, (address*)&amp;_active_table, sizeof(_active_table) / sizeof(address));  <br>
  }  <br>
}<br>
  <br>
// switch from the dispatch table which notices safepoints back to the  <br>
// normal dispatch table.  So that we can notice single stepping points,  <br>
// keep the safepoint dispatch table if we are single stepping in JVMTI.  <br>
// Note that the should_post_single_step test is exactly as fast as the  <br>
// JvmtiExport::_enabled test and covers both cases.  <br>
void TemplateInterpreter::ignore_safepoints() {  <br>
  if (_notice_safepoints) {  <br>
    if (!JvmtiExport::should_post_single_step()) {  <br>
      // switch to normal dispatch table  <br>
      _notice_safepoints = false;  <br>
      copy_table((address*)&amp;_normal_table, (address*)&amp;_active_table, sizeof(_active_table) / sizeof(address));  <br>
    }  <br>
  }  <br>
}<br>
</code></pre>

<h4>b. 运行在native code</h4>
如果线程运行在native code的时候，vm thread 是不需要等待线程执行完的，只需要在从native code 返回的时候去判断一下 <i>state 的状态就可以了。</i>

在方法体里就是前面博客也出现过的 SafepointSynchronize::do_call_back()<br>
<pre><code>inline static bool do_call_back() {  <br>
  return (_state != _not_synchronized);  <br>
}<br>
</code></pre>
判断了<code>_state</code> 不是<code>_not_synchronized</code>状态<br>
<br>
为了能让线程从native code 回到java 的时候为了能读到/设置正确线程的状态，通常的解决方法使用memory barrier，java 使用<code>OrderAccess::fence();</code> 在汇编里使用 <code>__asm__ volatile ("lock; addl $0,0(%%rsp)" : : : "cc", "memory");</code> 保证从内存里读到正确的值，但是这种方法严重影响系统的性能，于是java使用了每个线程都有独立的内存页来设置状态。通过使用使用参数-XX:+UseMembar  参数使用memory barrier，默认是不打开的，也就是使用独立的内存页来设置状态。<br>
<br>
<h4>c. 运行编译的代码</h4>

<h5>1. Poling page 页面</h5>
Poling page是在jvm初始化启动的时候会初始化的一个单独的内存页面，这个页面是让运行的编译过的代码的线程进入停止状态的关键。<br>
<br>
在linux里面使用了mmap初始化，源码如下<br>
<pre><code>address polling_page = (address) ::mmap(NULL, Linux::page_size(), PROT_READ, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);<br>
</code></pre>

<h5>2. 编译</h5>
java 的JIT 会直接编译一些热门的源码到机器码，直接执行而不需要在解释执行从而提高效率，在编译的代码中，当函数或者方法块返回的时候会去访问一个内存poling页面.<br>
<br>
x86架构下<br>
<pre><code>void LIR_Assembler::return_op(LIR_Opr result) {  <br>
  assert(result-&gt;is_illegal() || !result-&gt;is_single_cpu() || result-&gt;as_register() == rax, "word returns are in rax,");  <br>
  if (!result-&gt;is_illegal() &amp;&amp; result-&gt;is_float_kind() &amp;&amp; !result-&gt;is_xmm_register()) {  <br>
    assert(result-&gt;fpu() == 0, "result must already be on TOS");  <br>
  }  <br>
  <br>
  // Pop the stack before the safepoint code  <br>
  __ remove_frame(initial_frame_size_in_bytes());  <br>
  <br>
  bool result_is_oop = result-&gt;is_valid() ? result-&gt;is_oop() : false;  <br>
  <br>
  // Note: we do not need to round double result; float result has the right precision  <br>
  // the poll sets the condition code, but no data registers  <br>
  AddressLiteral polling_page(os::get_polling_page() + (SafepointPollOffset % os::vm_page_size()),  <br>
                              relocInfo::poll_return_type);  <br>
  <br>
  // NOTE: the requires that the polling page be reachable else the reloc  <br>
  // goes to the movq that loads the address and not the faulting instruction  <br>
  // which breaks the signal handler code  <br>
  <br>
  __ test32(rax, polling_page);  <br>
  <br>
  __ ret(0);  <br>
}<br>
</code></pre>

在前面提到的 <code>SafepointSynchronize::begin</code> 函数源码中<br>
<pre><code>if (UseCompilerSafepoints &amp;&amp; DeferPollingPageLoopCount &lt; 0) {  <br>
  // Make polling safepoint aware  <br>
  guarantee (PageArmed == 0, "invariant") ;  <br>
  PageArmed = 1 ;  <br>
  os::make_polling_page_unreadable();  <br>
}<br>
</code></pre>
这里提到了2个参数 <code>UseCompilerSafepoints</code> 和 <code>DeferPollingPageLoopCount</code> ，在默认的情况下这2个参数是true和-1<br>
<br>
函数体将会调用 <code>os:make_polling_page_unreadable();</code> 在linux os 下具体实现是调用了 <code>mprotect(bottom,size,prot)</code> 使polling 内存页变成不可读。<br>
<br>
<h5>3. 信号</h5>
到当编译好的程序尝试在去访问这个不可读的polling页面的时候，在系统级别会产生一个错误信号SIGSEGV, 可以参考笔者的一篇博客中曾经讲过java 的信号处理，可以知道信号SIGSEGV的处理函数在x86体系下见下源码：<br>
<pre><code>JVM_handle_linux_signal(int sig,  <br>
                        siginfo_t* info,  <br>
                        void* ucVoid,  <br>
                        int abort_if_unrecognized){  <br>
   ....  <br>
   if (sig == SIGSEGV &amp;&amp; os::is_poll_address((address)info-&gt;si_addr)) {  <br>
        stub = SharedRuntime::get_poll_stub(pc);  <br>
      }   <br>
   ....  <br>
}<br>
</code></pre>
在<code>linux x86,64 bit</code>的体系中，poll stub 的地址 就是 <code>SafepointSynchronize::handle_polling_page_exception</code> 详细程序可见 <code>shareRuntime_x86_64.cpp</code>

回到<code>safepoint.cpp</code>中，<code>SafepointSynchronize::handle_polling_page_exception</code> 通过取出线程的safepoint_stat,调用函数 <code>void ThreadSafepointState::handle_polling_page_exception</code>，最后通过调用 <code>SafepointSynchronize::block(thread());</code> 来block当前线程。<br>
<br>
<h4>d. block 状态</h4>
当线程进入block状态的时候，继续保持block状态。<br>
<hr />

<pre><code>enum Priorities {<br>
   SafepointPriority, // Highest priority (operation executed at a safepoint)<br>
   MediumPriority,    // Medium priority<br>
   nof_priorities<br>
};<br>
<br>
enum Mode {<br>
  _safepoint,       // blocking,        safepoint, vm_op C-heap allocated<br>
  _no_safepoint,    // blocking,     no safepoint, vm_op C-Heap allocated<br>
  _concurrent,      // non-blocking, no safepoint, vm_op C-Heap allocated<br>
  _async_safepoint  // non-blocking,    safepoint, vm_op C-Heap allocated<br>
};<br>
<br>
enum VMOp_Type {<br>
  VM_OPS_DO(VM_OP_ENUM)<br>
  VMOp_Terminating<br>
};<br>
</code></pre>

<table><thead><th> <b>VM_Operation</b> </th><th> <b>blocking</b> </th><th> <b>safepoint</b> </th><th> <b>详细说明</b> </th></thead><tbody>
<tr><td> <code>VM_Dummy</code> </td><td> <code>-</code> </td><td> <code>-</code> </td><td> 虚拟的VM operation，在循环双链表中作为第一个元素 </td></tr>
<tr><td> <code>VM_CMS_Operation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_CMS_Initial_Mark</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_CMS_Final_Remark</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_CGC_Operation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GC_Operation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_HeapDumper</code> </td><td>  </td><td>  </td><td> Dump内存到文件，以下情况会调用：<br><code>*</code> OutOfMemoryError && <code>-XX:+HeapDumpOnOutOfMemoryError</code><br><code>*</code> Full GC && <code>-XX:+HeapDumpBeforeFullGC</code><br><code>*</code> Full GC && <code>-XX:+HeapDumpAfterFullGC</code><br><code>*</code> <code>HotSpotDiagnostic#dumpHeap</code><br><code>*</code> <code>HotSpotVirtualMachine#dumpHeap</code>(如通过命令：<code>jmap -dump</code>) </td></tr>
<tr><td> <code>VM_GenCollectFullConcurrent</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_G1CollectFull</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_G1CollectForAllocation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_G1IncCollectionPause</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ParallelGCFailedAllocation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ParallelGCFailedPermanentAllocation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ParallelGCSystemGC</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GC_HeapInspection</code> </td><td>  </td><td>  </td><td> Dump内存Histogram到文件，以下情况会调用：<br><code>* </code> <code>kill -3</code><br><code>*</code> Full GC && <code>-XX:+PrintClassHistogramBeforeFullGC</code><br><code>*</code> Full GC && <code>-XX:+PrintClassHistogramAfterFullGC</code><br><code>*</code> <code>HotSpotVirtualMachine#heapHisto</code>(如通过命令：<code>jmap -histo</code>) </td></tr>
<tr><td> <code>VM_GenCollectForAllocation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GenCollectFull</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GenCollectForPermanentAllocation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_PopulateDumpSharedSpace</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_JNIFunctionTableCopier</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetOwnedMonitorInfo</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetObjectMonitorUsage</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetCurrentContendedMonitor</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetStackTrace</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetMultipleStackTraces</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetFrameCount</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetFrameLocation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_EnterInterpOnlyMode</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ChangeSingleStep</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetCurrentLocation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ChangeBreakpoints</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_RedefineClasses</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_GetOrSetLocal</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_HeapIterateOperation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_HeapWalkOperation</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_EnableBiasedLocking</code> </td><td> <code>is_cheap_allocated?</code><br><code>non-blocking:</code><br><code>blocking</code> </td><td> <code>safepoint</code> </td><td>  </td></tr>
<tr><td> <code>VM_RevokeBias</code> </td><td>  </td><td>  </td><td> 取消偏向锁，如计算HashCode，锁竞争等 </td></tr>
<tr><td> <code>VM_BulkRevokeBias</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ThreadStop</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ForceSafepoint</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ForceAsyncSafepoint</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_Deoptimize</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_DeoptimizeFrame</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_HandleFullCodeCache</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_DeoptimizeAll</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ZombieAll</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_Verify</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_PrintThreads</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_PrintJNI</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_FindDeadlocks</code> </td><td>  </td><td>  </td><td>  查找死锁线程 </td></tr>
<tr><td> <code>VM_ThreadDump</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_Exit</code> </td><td>  </td><td>  </td><td> 退出JVM  </td></tr>
<tr><td> <code>VM_DeoptimizeTheWorld</code> </td><td>  </td><td>  </td><td>  </td></tr>
<tr><td> <code>VM_ReportJavaOutOfMemory</code> </td><td>  </td><td>  </td><td> 在OOM时执行Shell命令，通过参数<code>-XX:OnOutOfMemoryError=</code>设置 </td></tr></tbody></table>

<h3>参将资料</h3>
<code>[1].</code> <a href='http://blog.csdn.net/raintungli/article/details/7162468'>http://blog.csdn.net/raintungli/article/details/7162468</a><br>