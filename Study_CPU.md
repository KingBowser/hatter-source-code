# brainiac vs speed-demon #

brainiac，追求instruction-level parallelism，speed-demon，追求更高的clock speed

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/cpu/brainiac.vs.speed-demon.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/cpu/brainiac.vs.speed-demon.jpg)

http://www.lighterra.com/papers/modernmicroprocessors/



---


# X86 #
X86或80X86，X86架构使用可变长度指令CISC(复杂指令集计算机,Complex Instruction Set Computer)。<br>
X86_32在Intel也称为IA32(Intel Architecture, 32-bit)，IA64<br>
<br>
<h4>通用寄存器</h4>
<table><thead><th> <b>寄存器</b> </th><th> <b>描述</b> </th></thead><tbody>
<tr><td> <code>AX/EAX/RAX</code> </td><td> 一般用作累加器 </td></tr>
<tr><td> <code>BX/EBX/RBX</code> </td><td> 一般用作基址寄存器（Base，用于数组） </td></tr>
<tr><td> <code>CX/ECX/RCX</code> </td><td> 一般用来计数（Count） </td></tr>
<tr><td> <code>DX/EDX/RDX</code> </td><td> 一般用来存放数据（Data） </td></tr>
<tr><td> <code>SI/ESI/RSI</code> </td><td> 一般用作源变址（Source Index） </td></tr>
<tr><td> <code>DI/EDI/RDI</code> </td><td> 一般用作目标变址（Destinatin Index） </td></tr>
<tr><td> <code>SP/ESP/RSP</code> </td><td> 一般用作堆栈指针（Stack Pointer） </td></tr>
<tr><td> <code>BP/EBP/RBP</code> </td><td> 一般用作基址指针（Base Pointer） </td></tr>
<tr><td> <code>IP/EIP/RIP</code> </td><td> Instruction pointer. Holds the program counter, the current instruction address. </td></tr></tbody></table>

<h4>段寄存器(Segment registers)</h4>
<table><thead><th> <b>寄存器</b> </th><th> <b>描述</b> </th></thead><tbody>
<tr><td> CS </td><td> 代码段寄存器 </td></tr>
<tr><td> DS </td><td> 数据段寄存器 </td></tr>
<tr><td> SS </td><td> 堆栈段寄存器 </td></tr>
<tr><td> ES </td><td> 附加数据段寄存器 </td></tr>
<tr><td> FS </td><td> 附加数据段寄存器 </td></tr>
<tr><td> GS </td><td> 附加数据段寄存器 </td></tr></tbody></table>

<h4>General Purpose Registers (<code>A, B, C and D</code>)</h4>
<table border='1'>
<blockquote><tr align='center'><th width='50'><code>64</code></th><th width='50'><code>56</code></th><th width='50'><code>48</code></th><th width='50'><code>40</code></th><th width='50'><code>32</code></th><th width='50'><code>24</code></th><th width='50'><code>16</code></th><th width='50'><code>8</code></th></tr>
<tr align='center'><td><code>R?X</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>E?X</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?X</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?H</code></td><td><code>?L</code></td></tr>
</table>
<br><br></blockquote>

<h4>64-bit mode-only General Purpose Registers (<code>R8, R9, R10, R11, R12, R13, R14, R15</code>)</h4>
<table border='1'>
<blockquote><tr align='center'><th width='50'><code>64</code></th><th width='50'><code>56</code></th><th width='50'><code>48</code></th><th width='50'><code>40</code></th><th width='50'><code>32</code></th><th width='50'><code>24</code></th><th width='50'><code>16</code></th><th width='50'><code>8</code></th></tr>
<tr align='center'><td><code>?</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?D</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?W</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?B</code></td></tr>
</table>
<br><br></blockquote>

<h4>Segment Registers (<code>C, D, S, E, F and G</code>)</h4>
<table border='1'>
<blockquote><tr align='center'><th width='50'><code>16</code></th><th width='50'><code>8</code></th></tr>
<tr align='center'><td><code>?S</code></td></tr>
</table>
<br><br></blockquote>

<h4>Pointer Registers (<code>S and B</code>)</h4>
<table border='1'>
<blockquote><tr align='center'><th width='50'><code>64</code></th><th width='50'><code>56</code></th><th width='50'><code>48</code></th><th width='50'><code>40</code></th><th width='50'><code>32</code></th><th width='50'><code>24</code></th><th width='50'><code>16</code></th><th width='50'><code>8</code></th></tr>
<tr align='center'><td><code>R?P</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>E?P</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?P</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?PL</code><sup>注1</sup></td></tr>
</table>
<br><br></blockquote>

<h4>Index Registers (<code>S and D</code>)</h4>
<table border='1'>
<blockquote><tr align='center'><th width='50'><code>64</code></th><th width='50'><code>56</code></th><th width='50'><code>48</code></th><th width='50'><code>40</code></th><th width='50'><code>32</code></th><th width='50'><code>24</code></th><th width='50'><code>16</code></th><th width='50'><code>8</code></th></tr>
<tr align='center'><td><code>R?I</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>E?I</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?I</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?IL</code><sup>注1</sup></td></tr>
</table>
<br><br></blockquote>

<h4>Instruction Pointer Register (<code>I</code>)</h4>
<table border='1'>
<blockquote><tr align='center'><th width='50'><code>64</code></th><th width='50'><code>56</code></th><th width='50'><code>48</code></th><th width='50'><code>40</code></th><th width='50'><code>32</code></th><th width='50'><code>24</code></th><th width='50'><code>16</code></th><th width='50'><code>8</code></th></tr>
<tr align='center'><td><code>R?P</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>E?P</code></td></tr>
<tr align='center'><td><code>--</code></td><td><code>?P</code></td></tr>
</table>
<br><br>
<sup>注1</sup>: The <code>?PL</code> <code>?IL</code> registers are only available in 64-bit mode.<br>
<br><br></blockquote>


Addressing modes for 16-bit x86 processors can be summarized by this formula:<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/addressing_mode_16bit.png' />


Addressing modes for 32-bit address size on 32-bit or 64-bit x86 processors can be summarized by this formula:<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/addressing_mode_32bit.png' />


Addressing modes for 64-bit code on 64-bit x86 processors can be summarized by this formula:<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/addressing_mode_64bit.png' />

<h4>Instruction suffixes <sup>[5]</sup></h4>
<table><thead><th> <b>b</b> </th><th> byte </th></thead><tbody>
<tr><td> <b>w</b> </td><td> word (2 bytes) </td></tr>
<tr><td> <b>l</b> </td><td> long (4 bytes) </td></tr></tbody></table>

<h4>Condition codes <sup>[5]</sup></h4>
<table><thead><th> <b>CF</b> </th><th> Carry Flag </th></thead><tbody>
<tr><td> <b>ZF</b> </td><td> Zero Flag </td></tr>
<tr><td> <b>SF</b> </td><td> Sign Flag </td></tr>
<tr><td> <b>OF</b> </td><td> Overflow Flag </td></tr></tbody></table>

<h3>IA32 Addressing modes <sup>[5]</sup></h3>
<h4>Immediate</h4>
<table><thead><th> <code>$val Val</code> </th></thead><tbody>
<tr><td> val: constant integer value </td></tr>
<tr><td> <code>movl $17, %eax</code> </td></tr></tbody></table>

<h4>Normal</h4>
<table><thead><th> <code>(R) Mem[Reg[R]]</code> </th></thead><tbody>
<tr><td> R: register R specifies memory address </td></tr>
<tr><td> <code>movl (%ecx), %eax</code> </td></tr></tbody></table>

<h4>Displacement</h4>
<table><thead><th> <code>D(R) Mem[Reg[R]+D]</code> </th></thead><tbody>
<tr><td> R: register specifies start of memory region </td></tr>
<tr><td> D: constant displacement D specifies offset </td></tr>
<tr><td> movl 8(%ebp), %edx </td></tr></tbody></table>

<h4>Indexed</h4>
<table><thead><th> <code>D(Rb,Ri,S) Mem[Reg[Rb]+S*Reg[Ri]+D]</code> </th></thead><tbody>
<tr><td> D: constant displacement 1, 2, or 4 bytes </td></tr>
<tr><td> Rb: base register: any of 8 integer registers </td></tr>
<tr><td> Ri: index register: any, except %esp </td></tr>
<tr><td> S: scale: 1, 2, 4, or 8 </td></tr>
<tr><td> <code>movl 0x100(%ecx,%eax,4), %edx</code> </td></tr></tbody></table>

<h2>Processor affinity</h2>
Processor affinity or CPU Pinning. CPU pinning enables mapping and unmapping entire virtual machines or a specific virtual CPU (vCPU), to a physical CPU or a range of CPUs. <sup>[7]</sup><br>
<br>
<br>
<h3>参考资料</h3>
<code>[1].</code> <a href='http://zh.wikipedia.org/wiki/X86'>http://zh.wikipedia.org/wiki/X86</a><br>
<code>[2].</code> <a href='http://en.wikipedia.org/wiki/X86'>http://en.wikipedia.org/wiki/X86</a><br>
<code>[3].</code> <a href='http://www.intel.com/content/www/us/en/processors/architectures-software-developer-manuals.html'>http://www.intel.com/content/www/us/en/processors/architectures-software-developer-manuals.html</a><br>
<code>[4].</code> <a href='http://www.cs.virginia.edu/~evans/cs216/guides/x86.html'>http://www.cs.virginia.edu/~evans/cs216/guides/x86.html</a><br>
<code>[5].</code> <a href='http://www.stanford.edu/class/cs107/other/IA32_Cheat_Sheet.pdf'>http://www.stanford.edu/class/cs107/other/IA32_Cheat_Sheet.pdf</a><br>
<code>[6].</code> <a href='http://en.wikipedia.org/wiki/Assembly_language'>http://en.wikipedia.org/wiki/Assembly_language</a><br>
<code>[7].</code> <a href='http://en.wikipedia.org/wiki/Processor_affinity'>http://en.wikipedia.org/wiki/Processor_affinity</a><br>