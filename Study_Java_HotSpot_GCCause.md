| **Enum** | **Readable** | **Description** |
|:---------|:-------------|:----------------|
| `_no_gc` | `No GC` | 未发生过GC |
| `_java_lang_system_gc` | `System.gc()` | 通过`System.gc()`触发的GC，在HotSpot虚拟机中该操作触发FullGC，通过`-XX:-DisableExplicitGC`可以禁止该操作触发FullGC |
| `_full_gc_alot` | `FullGCAlot` |  |
| `_scavenge_alot` | `ScavengeAlot` |  |
| `_allocation_profiler` | `Allocation Profiler` |  |
| `_jvmti_force_gc` | `JvmtiEnv ForceGarbageCollection` | 通过jvmti调用`JvmtiEnv::ForceGarbageCollection()`触发的强制GC |
| `_allocation_failure` | `Allocation Failure` |  |
| `_gc_locker` | `GCLocker Initiated GC` |  |
| `_heap_inspection` | `Heap Inspection Initiated GC` | 在使用命令`jmap -histo:live`时触发的GC |
| `_heap_dump` | `Heap Dump Initiated GC` | 在使用命令`jmap -dump:live,`时触发的GC |
| `_tenured_generation_full` | `Tenured Generation Full` |  |
| `_permanent_generation_full` | `Permanent Generation Full` |  |
| `_cms_generation_full` | `CMS Generation Full` |  |
| `_cms_initial_mark` | `CMS Initial Mark` |  |
| `_cms_final_remark` | `CMS Final Remark` |  |
| `_old_generation_expanded_on_last_scavenge` | `Old Generation Expanded On Last Scavenge` |  |
| `_old_generation_too_full_to_scavenge` | `Old Generation Too Full To Scavenge` |  |
| `_last_ditch_collection` | `Last ditch collection` |  |
| `_last_gc_cause` | `ILLEGAL VALUE - last gc cause - ILLEGAL VALUE` |  |
| `*other*` | `unknown GCCause` |  |