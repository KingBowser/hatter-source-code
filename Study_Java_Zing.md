Azul创新的Zing JVM和无停顿垃圾回收（GC）使Apache的 Lucene 项目开发者开始去研究需要大规模堆的事例（例如为了更快搜索将整个搜索索引存在内存中）。基于全维基百科英文站点的索引内存初步测试显示Zing真正实现了在管理140GB以上堆时不用暂停。

Zing是基于Oracle Hotspot虚拟机的为Linux和x86高度优化的100% Java兼容的Java虚拟机。

开源版：

https://github.com/GregBowyer/ManagedRuntimeInitiative


### 参考资料 ###
`[1].` http://www.azulsystems.com/products/zing/whatisit<br>
<code>[2].</code> <a href='http://www.azulsystems.com/products/zing/faq'>http://www.azulsystems.com/products/zing/faq</a><br>
<code>[3].</code> <a href='http://www.infoq.com/cn/news/2012/09/azul-zing-free'>http://www.infoq.com/cn/news/2012/09/azul-zing-free</a><br>