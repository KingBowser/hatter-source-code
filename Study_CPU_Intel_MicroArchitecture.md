# X86微架构 #
不完整的Intel CPU微架构：
![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/intel_processor_roadmap.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/intel_processor_roadmap.png)

| **微架构** | **说明** |
|:--------------|:-----------|
| `8086` |  |
| `186` |  |
| `286` |  |
| `i386` |  |
| `i486` |  |
| `P5` |  |
| `P6` |  |
| `Netburst` |  |
| `Pentium-M` |  |
| `Core (Merom, Penryn)` |  |
| `Nehalem (Nehalem, Westmere)` |  |
| `Sandy Bridge (Sandy Bridge, Ivy Bridge)` |  |
| `Haswell (Haswell, Rockwell)` |  |
| `Skylake` |  |
| `Larrabee` |  |
| `Bonnell` |  |

# 安腾微架构 #
| **微架构** | **说明** |
|:--------------|:-----------|
| `Merced` |  |
| `McKinley` |  |
| `Tukwila` |  |
| `Poulson` |  |



---

```
（1）x86 （IA-32，i386，x86-32，x32）
x86是指基于Intel公司在1978年发布的8086的一系列32位处理器架构。x86命令的由来是，因为Intel早期的一系列处理器代号都以”86″为结尾，如：8086, 80186, 80286, 80386, 80486；“x86”这个术语也是在i386时代开始流行起来的。当时以i386为代表的Intel处理器成为了PC时代的处理器的代名词，以至于现在很多32为处理器上的一些软件仍然采用i386作为表示它是在Intel/AMD的32位处理器的上运行。Intel的i386、i486等都是完全向后兼容的，甚至兼容最初的8086处理器，理论上来说，在8086处理器上编译的程序，放到今天的在Intel处理器上运行的32bit的操作系统上仍然可以运行。主要是Intel、AMD（包括AVI）、Citrix等公司在生产x86架构的处理器。
其中IA是Intel Architecture的缩写，i386中的”i”应该是值Intel。
物理地址大小为32位，可寻址的内存为4GB；而有PAE（Physical Address Extension）技术的支持，物理地址可达到36位，支持最大64GB的内存寻址。（不论是否开启PAE，32位的x86处理器的虚拟地址仍然为32位，所以单个进程仍然最多可以使用4GB的内存。）

（2）x86-64 （Intel64/AMD64，IA-32e，EM64T,x64）
x86-64是指与以前x86-32兼容的Intel的64位处理器架构。其实，它也是叫“AMD64”，因为最初是AMD公司设计和生产的，后来Intel公司也加入到这个行列且迅速成为x86-64平台的最大玩家。由于与32位的x86完全兼容，所以在Intel 32bit处理器上能正常运行的程序，理论上来说，在x86-64平台上也是可以完全正常运行的。主要有Intel和AMD两大公司生产x86-64架构的处理器，x86-64也是目前服务器、台式机、笔记本市场中的最普及的处理器架构。
IA32-e: extension for IA32.
EM64T: extended memory 64 technology.
x64（包括上一个的x32），一般是在微软的产品（如：Windows、Office）等中这么来称呼Intel的64位处理器（或x32值Intel 32位处理器）。
x86-64最大支持52位的物理地址（而目前，AMD有48位的，Intel有36位、40位、46位等等规格的物理地址大小），支持物理内存可达到256TB（以目前最多的48位计算）。它的虚拟地址大小是最大是64位，就目前的设计和实现而言，目前Intel和AMD只使用了其中的低48位。

（3）IA-64 （Itanium，安腾）
Intel的安腾处理器架构，与x86、x86-64架构完全不兼容，它主要是面向高性能计算设计的处理架构。它最初是由HP公司研发的，后来Intel与HP联合研发安腾处理器。目前，Intel对安腾的支持也非常的少的，现在还有HP公司坚持着做一些IA-64的研发。Microsoft、Redhat等著名的操作系统厂商都曾经研发过支持Itanium处理器的操作系统，不过，就目前的形势来看，安腾的处境非常不妙，对它的支持会越来越少，之后几年后将会淡出历史舞台。

另外，”x86″有时也指上面（1）和（2）两种，包括32位的x86-32和64位的x86-64；与8086/i386等兼容的处理器架构有时被统称为x86架构。
```


### 参考资料 ###
`[1].` http://www.360doc.com/content/10/0929/13/158286_57292837.shtml<br>
<code>[2].</code> <a href='http://en.wikipedia.org/wiki/List_of_Intel_CPU_microarchitectures'>http://en.wikipedia.org/wiki/List_of_Intel_CPU_microarchitectures</a><br>
<code>[3].</code> <a href='http://en.wikipedia.org/wiki/List_of_Intel_Xeon_microprocessors'>http://en.wikipedia.org/wiki/List_of_Intel_Xeon_microprocessors</a><br>
<code>[4].</code> <a href='http://smilejay.com/2012/07/intel-procssor-architecture/'>http://smilejay.com/2012/07/intel-procssor-architecture/</a><br>