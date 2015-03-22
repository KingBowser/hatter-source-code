# MESI protocol #
缓存一致性(MESI, Modified Exclusive Shared Invalid)协议，也称为伊利诺伊协议(Illinois protocol，由伊利诺伊大学香槟`[Urbana-Champaign]`分校开发)，被广泛用于缓存一致性和内存一致性。是支持缓存回写的最通用的一种协议。 <sup>[1]</sup><br>

在MESI协议中，每个缓存行有4个状态，可用2个bit表示，它们分别是 <sup>[7]</sup>：<br>
<table><thead><th> <b>状态</b> </th><th> <b>描述</b> </th></thead><tbody>
<tr><td> M(Modified) </td><td> 这行数据有效，数据被修改了，和内存中的数据不一致，数据只存在于本Cache中。 </td></tr>
<tr><td> E(Exclusive) </td><td> 这行数据有效，数据和内存中的数据一致，数据只存在于本Cache中。 </td></tr>
<tr><td> S(Shared) </td><td> 这行数据有效，数据和内存中的数据一致，数据存在于很多Cache中。 </td></tr>
<tr><td> I(Invalid) </td><td> 这行数据无效 </td></tr></tbody></table>

状态迁移图：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/mesi.jpeg' /><br>

<h2>MESI-F <sup>[6][10]</sup></h2>
<blockquote>MESI-F可以说是Intel在多Xeon使用的MESI协议的扩充，增加了一个F状态（同时修改了S状态让其无法转发以避免进行过多的传输）。F状态就是这样一个状态：在一个多处理器之间共享的缓存页面中，只有其中一个处理器的该页面处于F状态，另外所有处理器的该页面均处于S状态，F状态负责响应其他没有该页面的处理器的读请求，而S状态则不响应并且不允许将缓存页面发给他人（或许S用Silent来代表更合适）。</blockquote>

<blockquote>当一个新处理器需求读取这个F页面时，原有的F页面则转为S状态，新的处理器获得的页面总是保持为F状态。在一群相同的页面中总有并且只有一个页面是处于F状态，其他的S副本则以F副本为中心。这种流动性让传输压力得以分散到各个处理器上，而不是总维持在原始页面上。</blockquote>

<blockquote>不会改变的页面的共享很好处理，关键的是对Dirty页面的对待（Dirty页面是指一个内容被修改了的缓存页面，需要更新到内存里面去），显然，一堆页面的副本中同一时间内只能有其中一个可以被写入。MESI-F中，只具有一个副本的E状态在被写入的时候只需要简单地转化为M状态；而F状态被写入时则会导致其所有的S副本都被置为无效（通过一个广播完成）；S副本是“沉默”的，不允许转发，也不允许被写入，这些副本所在的处理器要再次使用这个副本时，需要再次向原始F副本请求，F副本现在已经转化为M副本，被请求状态下M副本会写入内存并重新转化为F状态，不被请求时则可以保持在M状态，并可以不那么快地写入内存以降低对内存带宽的占用。</blockquote>

<blockquote>MESI-F实际上只允许一堆共享副本当中的中央副本（F状态）被写入，在多个处理器均需要写入一个缓存页面的时候，会引起“弹跳”现象，F副本在各个处理器之间不停传输——这有点像令牌环——会降低性能，特别是F副本不在其所在的原始内存空间的时候。</blockquote>

<h2>MOESI</h2>
MOSEI协议不需要被写入的M状态写入内存就可以进行共享（这时M状态会转变为O状态，共享后的Dirty副本被标记为S状态），这避免了一次写入内存，节约了一些开销；当再次写入O状态副本时，其他的S副本同样会被设置为无效。MOSEI也只允许一堆共享副本当中的中央副本（O状态）被写入，也存在着弹跳现象。 <sup>[10]</sup><br>
<br>
<h2><i>MOSI</i></h2>
<h2><i>MSI</i></h2>
<br><br>

MESI、MESI-F、MOESI状态机的比较： <sup>[9]</sup><br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/mesi_foc.gif' />

<br>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://en.wikipedia.org/wiki/MESI_protocol'>http://en.wikipedia.org/wiki/MESI_protocol</a><br>
<code>[2].</code> <a href='http://en.wikipedia.org/wiki/MESIF_protocol'>http://en.wikipedia.org/wiki/MESIF_protocol</a><br>
<code>[3].</code> <a href='http://en.wikipedia.org/wiki/MOESI_protocol'>http://en.wikipedia.org/wiki/MOESI_protocol</a><br>
<code>[4].</code> <a href='http://en.wikipedia.org/wiki/MOSI_protocol'>http://en.wikipedia.org/wiki/MOSI_protocol</a><br>
<code>[5].</code> <a href='http://en.wikipedia.org/wiki/MSI_protocol'>http://en.wikipedia.org/wiki/MSI_protocol</a><br>
<code>[6].</code> <a href='http://en.wikipedia.org/wiki/Non-Uniform_Memory_Access#Cache_coherent_NUMA_.28ccNUMA.29'>http://en.wikipedia.org/wiki/Non-Uniform_Memory_Access#Cache_coherent_NUMA_.28ccNUMA.29</a><br>
<code>[7].</code> <a href='http://www.tektalk.org/2011/07/11/cache%E4%B8%80%E8%87%B4%E6%80%A7%E5%8D%8F%E8%AE%AE%E4%B8%8Emesi2/'>http://www.tektalk.org/2011/07/11/cache%E4%B8%80%E8%87%B4%E6%80%A7%E5%8D%8F%E8%AE%AE%E4%B8%8Emesi2/</a><br>
<code>[8].</code> <a href='http://blog.csdn.net/zhuliting/article/details/6210921'>http://blog.csdn.net/zhuliting/article/details/6210921</a><br>
<code>[9].</code> <a href='http://www.tektalk.org/2010/12/05/%E6%B5%85%E8%B0%88intel-qpi%E7%9A%84mesfi%E5%8D%8F%E8%AE%AE/'>http://www.tektalk.org/2010/12/05/%E6%B5%85%E8%B0%88intel-qpi%E7%9A%84mesfi%E5%8D%8F%E8%AE%AE/</a><br>
<code>[10].</code> <a href='http://server.it168.com/a2008/1224/261/000000261184_all.shtml'>http://server.it168.com/a2008/1224/261/000000261184_all.shtml</a><br>