<b>The Serviceability Agent(SA).</b> The Serviceability Agent is a Sun private component in the HotSpot repository that was developed by HotSpot engineers to assist in debugging HotSpot. They then realized that SA could be used to craft serviceability tools for end users since it can expose Java objects as well as HotSpot data structures both in running processes and in core files.

## `HotSpot SA` ##
```
alias clhsdb='java -classpath .:$JAVA_HOME/lib/sa-jdi.jar sun.jvm.hotspot.CLHSDB'
alias ssum="awk '{sum+=\$1}; END {print sum}'"
alias objectsizesumm="grep 'object size' | awk -F 'object size = ' '{print \$2}' | awk -F ')' '{print \$1}' | ssum"
http://javasourcecode.org/html/open-source/jdk/jdk-6u23/sun/jvm/hotspot/CLHSDB.java.html
http://rednaxelafx.iteye.com/blog/730461
https://gist.github.com/956694
```

  * <b><code>sun.jvm.hotspot.CLHSDB</code></b> <br>命令行界面HotSpotDeBugger<br>
<ul><li><b><code>sun.jvm.hotspot.HSDB</code></b> <br>图形界面HotSpotDeBugger</li></ul>

<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/oop_klass.png' />

<ul><li><font color='red'><b><code>sun.jvm.hotspot.tools.Tool</code></b></font>
<ul><li><code>sun.jvm.hotspot.tools.FinalizerInfo</code> <br>查找 <code>java.lang.ref.Finalizer</code> 下的queue中的队列信息<br>
</li><li><code>sun.jvm.hotspot.tools.FlagDumper</code> <br>通过 <code>VM.getVM().getCommandLineFlags()</code> 打印所有的flag信息<br>
</li><li><code>sun.jvm.hotspot.tools.HeapDumper</code> <br>Dump内存数据为文件<br>
</li><li><code>sun.jvm.hotspot.tools.HeapSummary</code> <br>打印内存分配(分带)情况<br>
</li><li><code>sun.jvm.hotspot.tools.JInfo</code> <br>命令<code>jinfo</code>
</li><li><code>sun.jvm.hotspot.tools.JMap</code> <br>命令<code>jmap</code>
</li><li><code>sun.jvm.hotspot.tools.JSnap</code> <br>打印Perf信息<br>
</li><li><code>sun.jvm.hotspot.tools.JStack</code> <br>打印JVM的StackTrace<br>
</li><li><code>sun.jvm.hotspot.tools.ObjectHistogram</code> <br>打印'jmap -histo'信息<br>
</li><li><code>sun.jvm.hotspot.tools.PermStat</code>
</li><li><code>sun.jvm.hotspot.tools.PMap</code>
</li><li><code>sun.jvm.hotspot.tools.PStack</code>
</li><li><code>sun.jvm.hotspot.tools.StackTrace</code>
</li><li><code>sun.jvm.hotspot.tools.SysPropsDumper</code> <br>通过 <code>VM.getVM().getSystemProperties()</code> 打印 <code>Properties getProperties()</code> 的信息<br>
</li><li><code>sun.jvm.hotspot.tools.jcore.ClassDump</code> <br>打印内存中的class内存到文件<br>
</li><li><code>sun.jvm.hotspot.tools.joql.JSDB</code>
</li><li><code>sun.jvm.hotspot.tools.joql.JOQL</code></li></ul></li></ul>

<ul><li><font color='red'><b><code>sun.jvm.hotspot.runtime.VM</code></b></font></li></ul>

<table><thead><th> <code>isCore</code> </th><th> <code>boolean</code> </th><th>  </th></thead><tbody>
<tr><td> <code>isClientCompiler</code> </td><td> <code>boolean</code> </td><td> 是否是C1(client)编译器 </td></tr>
<tr><td> <code>isServerCompiler</code> </td><td> <code>boolean</code> </td><td> 是否是C2(server)编译器 </td></tr>
<tr><td> <code>useDerivedPointerTable</code> </td><td> <code>boolean</code> </td><td>  </td></tr>
<tr><td> <code>isBigEndian</code> </td><td> <code>boolean</code> </td><td> 是否是BigEndian </td></tr>
<tr><td> <code>isLP64</code> </td><td> <code>boolean</code> </td><td> 是否是64位JVM </td></tr>
<tr><td> <code>isSharingEnabled</code> </td><td> <code>boolean</code> </td><td>  </td></tr>
<tr><td> <code>isCompressedOopsEnabled</code> </td><td> <code>boolean</code> </td><td>  </td></tr>
<tr><td> <code>getUseTLAB</code> </td><td> <code>boolean</code> </td><td> 是否使用TLAB(Thread Local Allocation Buffer) </td></tr>
<tr><td> <code>isDebugging</code> </td><td> <code>boolean</code> </td><td>  </td></tr>
<tr><td> <code>wizardMode</code> </td><td> <code>boolean</code> </td><td>  </td></tr>
<tr><td> <code>getInvocationEntryBCI</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getInvalidOSREntryBCI</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getObjectAlignmentInBytes</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getAddressSize</code> </td><td> <code>long</code> </td><td>  </td></tr>
<tr><td> <code>getOopSize</code> </td><td> <code>long</code> </td><td>  </td></tr>
<tr><td> <code>getLogAddressSize</code> </td><td> <code>long</code> </td><td>  </td></tr>
<tr><td> <code>getIntSize</code> </td><td> <code>long</code> </td><td>  </td></tr>
<tr><td> <code>getStackBias</code> </td><td> <code>long</code> </td><td>  </td></tr>
<tr><td> <code>getBytesPerLong</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getMinObjAlignmentInBytes</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getLogMinObjAlignmentInBytes</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getHeapWordSize</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getHeapOopSize</code> </td><td> <code>int</code> </td><td>  </td></tr>
<tr><td> <code>getOS</code> </td><td> <code>java.lang.String</code> </td><td> 操作系统: <code>solaris, linux, win32</code> </td></tr>
<tr><td> <code>getCPU</code> </td><td> <code>java.lang.String</code> </td><td> CPU架构: <code>x86, sparc, ia64, amd64(x86_64)</code> </td></tr>
<tr><td> <code>getVMRelease</code> </td><td> <code>java.lang.String</code> </td><td>  </td></tr>
<tr><td> <code>getVMInternalInfo</code> </td><td> <code>java.lang.String</code> </td><td>  </td></tr>
<tr><td> <code>getVM</code> </td><td> <code>sun.jvm.hotspot.runtime.VM</code> </td><td> <code>static</code>: VM类singgleton实例 </td></tr>
<tr><td> <code>getBytes</code> </td><td> <code>sun.jvm.hotspot.runtime.Bytes</code> </td><td>  </td></tr>
<tr><td> <code>getThreads</code> </td><td> <code>sun.jvm.hotspot.runtime.Threads</code> </td><td>  </td></tr>
<tr><td> <code>getTypeDataBase</code> </td><td> <code>sun.jvm.hotspot.types.TypeDataBase</code> </td><td>  </td></tr>
<tr><td> <code>getUniverse</code> </td><td> <code>sun.jvm.hotspot.memory.Universe</code> </td><td>  </td></tr>
<tr><td> <code>getObjectHeap</code> </td><td> <code>sun.jvm.hotspot.oops.ObjectHeap</code> </td><td> Heap区对象 </td></tr>
<tr><td> <code>getSymbolTable</code> </td><td> <code>sun.jvm.hotspot.memory.SymbolTable</code> </td><td> JVM的符号池 </td></tr>
<tr><td> <code>getStringTable</code> </td><td> <code>sun.jvm.hotspot.memory.StringTable</code> </td><td> JVM的字符串常量池 </td></tr>
<tr><td> <code>getSystemDictionary</code> </td><td> <code>sun.jvm.hotspot.memory.SystemDictionary</code> </td><td>  </td></tr>
<tr><td> <code>getObjectSynchronizer</code> </td><td> <code>sun.jvm.hotspot.runtime.ObjectSynchronizer</code> </td><td>  </td></tr>
<tr><td> <code>getJNIHandles</code> </td><td> <code>sun.jvm.hotspot.runtime.JNIHandles</code> </td><td>  </td></tr>
<tr><td> <code>getInterpreter</code> </td><td> <code>sun.jvm.hotspot.interpreter.Interpreter</code> </td><td>  </td></tr>
<tr><td> <code>getStubRoutines</code> </td><td> <code>sun.jvm.hotspot.runtime.StubRoutines</code> </td><td>  </td></tr>
<tr><td> <code>getVMRegImplInfo</code> </td><td> <code>sun.jvm.hotspot.code.VMRegImpl</code> </td><td>  </td></tr>
<tr><td> <code>getCodeCache</code> </td><td> <code>sun.jvm.hotspot.code.CodeCache</code> </td><td>  </td></tr>
<tr><td> <code>getRuntime1</code> </td><td> <code>sun.jvm.hotspot.c1.Runtime1</code> </td><td>  </td></tr>
<tr><td> <code>getDebugger</code> </td><td> <code>sun.jvm.hotspot.debugger.JVMDebugger</code> </td><td>  </td></tr>
<tr><td> <code>getRevPtrs</code> </td><td> <code>sun.jvm.hotspot.utilities.ReversePtrs</code> </td><td>  </td></tr>
<tr><td> <code>getCommandLineFlags</code> </td><td> <code>sun.jvm.hotspot.runtime.VM$Flag[]</code> </td><td> JVM的各种参数  </td></tr>
<tr><td> <code>getSystemProperties</code> </td><td> <code>java.util.Properties</code> </td><td> System.getProperties()的各种参数 </td></tr></tbody></table>


<ul><li><b><code>sun.jvm.hotspot.utilities.*</code></b>
<ul><li><code>CStringUtilities</code>
</li><li><code>ObjectReader</code>
</li><li><code>SystemDictionaryHelper</code>
</li></ul></li><li><b><code>sun.jvm.hotspot.oops.*</code></b>
<ul><li><code>OopUtilities</code></li></ul></li></ul>


Visitors:<br>
<pre><code>sun.jvm.hotspot.asm.InstructionVisitor<br>
	public interface InstructionVisitor {<br>
sun.jvm.hotspot.code.CodeCacheVisitor<br>
	public interface CodeCacheVisitor {<br>
sun.jvm.hotspot.compiler.OopMapVisitor<br>
	public interface OopMapVisitor {<br>
sun.jvm.hotspot.debugger.cdbg.LineNumberVisitor<br>
	public interface LineNumberVisitor {<br>
sun.jvm.hotspot.debugger.cdbg.ObjectVisitor<br>
	public interface ObjectVisitor {<br>
sun.jvm.hotspot.debugger.cdbg.TypeVisitor<br>
	public interface TypeVisitor {<br>
sun.jvm.hotspot.interpreter.BytecodeVisitor<br>
	public interface BytecodeVisitor {<br>
sun.jvm.hotspot.memory.StringTable<br>
	public interface StringVisitor {<br>
sun.jvm.hotspot.memory.SymbolTable<br>
	public interface SymbolVisitor {<br>
sun.jvm.hotspot.memory.SystemDictionary<br>
	public static interface ClassVisitor {<br>
	public static interface ClassAndLoaderVisitor {<br>
sun.jvm.hotspot.oops.HeapVisitor<br>
	public interface HeapVisitor {<br>
sun.jvm.hotspot.oops.OopVisitor<br>
	public interface OopVisitor {<br>
sun.jvm.hotspot.oops.RawHeapVisitor<br>
	public interface RawHeapVisitor extends AddressVisitor {<br>
sun.jvm.hotspot.runtime.AddressVisitor<br>
	public interface AddressVisitor {<br>
sun.jvm.hotspot.runtime.PerfMemory<br>
	public static interface PerfDataEntryVisitor {<br>
sun.jvm.hotspot.utilities.soql.ObjectVisitor<br>
	public interface ObjectVisitor {<br>
</code></pre>


Sample:<br>
<pre><code>  private void readSystemProperties() {<br>
     InstanceKlass systemKls = getSystemDictionary().getSystemKlass();<br>
     systemKls.iterate(new DefaultOopVisitor() {<br>
                               ObjectReader objReader = new ObjectReader();<br>
                               public void doOop(sun.jvm.hotspot.oops.OopField field, boolean isVMField) {<br>
                                  if (field.getID().getName().equals("props")) {<br>
                                     try {<br>
                                        sysProps = (Properties) objReader.readObject(field.getValue(getObj()));<br>
                                     } catch (Exception e) {<br>
                                        if (Assert.ASSERTS_ENABLED) {<br>
                                           e.printStackTrace();<br>
                                        }<br>
                                     }<br>
                                  }<br>
                               }<br>
                        }, false);<br>
  }<br>
</code></pre>


<h3>参数资料</h3>
<code>[1].</code> openjdk-6-src-b20-21_jun_2010.tar.gz<br>
<code>[2].</code> <a href='http://openjdk.java.net/groups/hotspot/docs/Serviceability.html'>http://openjdk.java.net/groups/hotspot/docs/Serviceability.html</a><br>
<code>[3].</code> <a href='http://rednaxelafx.iteye.com/blog/730461'>http://rednaxelafx.iteye.com/blog/730461</a><br>