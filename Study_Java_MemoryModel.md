[Memory Model](http://www.cs.umd.edu/~pugh/java/memoryModel/) >> [Cookbook](http://gee.cs.oswego.edu/dl/jmm/cookbook.html)

<table border='1'>
<tr>
<blockquote><td><b>Can Reorder</b></td>
<td align='center'> <i>2nd operation</i> </td></tr>
<tr>
<td> <i>1st operation</i> </td>
<td><code>Normal Load, Normal Store</code></td>
<td><code>Volatile Load, MonitorEnter</code></td>
<td><code>Volatile Store, MonitorExit</code></td>
</tr>
<tr>
<td><code>Normal Load, Normal Store</code></td>
<td> </td>
<td> </td>
<td>No</td>
</tr>
<tr>
<td><code>Volatile Load, MonitorEnter</code></td>
<td>No</td>
<td>No</td>
<td>No</td>
</tr>
<tr>
<td><code>Volatile store, MonitorExit</code></td>
<td> </td>
<td>No</td>
<td>No</td>
</tr>
</table></blockquote>

| **Processor** | **LoadStore** | **LoadLoad** | **StoreStore** | **StoreLoad** | **Data dependency orders loads?** | **Atomic Conditional** | **Other Atomics** | **Atomics provide barrier?** |
|:--------------|:--------------|:-------------|:---------------|:--------------|:----------------------------------|:-----------------------|:------------------|:-----------------------------|
| `sparc-TSO` | `no-op` | `no-op` | `no-op` | `membar(StoreLoad)` | `yes` | `CAS:casa` | `swap, ldstub` | `full` |
| `x86` | `no-op` | `no-op` | `no-op` | `mfence or cpuid or locked insn` | `yes` | `CAS: cmpxchg` | `xchg, locked insn` | `full` |
| `ia64` | `combine with st.rel or ld.acq` | `ld.acq` | `st.rel` | `mf` | `yes` | `CAS:cmpxchg` | `xchg,fetchadd` | `target +acq/rel` |


### 参考资料 ###
`[1].` http://gee.cs.oswego.edu/dl/jmm/cookbook.html<br>
<code>[2].</code> <a href='http://www.cs.umd.edu/~pugh/java/memoryModel/'>http://www.cs.umd.edu/~pugh/java/memoryModel/</a><br>