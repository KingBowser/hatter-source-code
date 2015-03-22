# Introduction #

You can use jflg show or set HotSpot JVM flags


# Details #

Usage:
```
wget https://hatter-source-code.googlecode.com/svn/trunk/javatools/jflag/jflagall.jar
```
```
java -jar jflagall.jar
```
```
Usage:
  java -jar jflagall.jar [options] <pid>
    -show <flags>             show flags('ALL' show all)
    -flag <+/-flag>           set flag
          <+/-~flag>          set flag by JMX
    -runt <option>            runtime(default all)
          options             [product, lp64_product, product_pd, product_rw, manageable, diagnostic, develop, develop_pd, notproduct, experimental]
    -type <option>            type(default all)
          options             [ccstr, ccstrlist, intx, uint64_t, uintx, bool, double]
    --show-not-exists         show not exists flag(s)
    --show-cust-flags         show custom flags
    --show-remote-flags       show remote flags
    --show-remote-writable    show writable remote flags

Sample:
  java -jar jflagall.jar --show-remote-flags --show-remote-writable <PID>  show remote JVM all writeable flags
  java -jar jflagall.jar --show-cust-flags                                 show supported custome flags
  java -jar jflagall.jar -flag +PrintGCDetails <PID>                       open JVM's PrintGCDetails flag
  java -jar jflagall.jar -flag +~TraceClassLoading <PID>                   open JVM's TraceClassLoading flag using JMX

jvmid     attachable   classname
1338      true         sun.tools.jconsole.JConsole
278       true         
```

```
$ java -jar jflagall.jar -show Trace 1557
[INFO] Attach to vm: 1557
FlagName                                 Type                Runtime             Value
-------------------------------------------------------------------------------------------
JavaMonitorsInStackTrace                 bool                product             true
TraceSuspendWaitFailures                 bool                product             false
StackTraceInThrowable                    bool                product             true
OmitStackTraceInFastThrow                bool                product             true
TraceJVMTI                               ccstr               product             
TraceRedefineClasses                     intx                product             0
TraceClassResolution                     bool                product             false
TraceBiasedLocking                       bool                product             false
TraceMonitorInflation                    bool                product             false
TraceClassLoading                        bool                product_rw          false
TraceClassLoadingPreorder                bool                product             false
TraceClassUnloading                      bool                product_rw          false
TraceLoaderConstraints                   bool                product_rw          false
TraceGen0Time                            bool                product             false
TraceGen1Time                            bool                product             false
TraceParallelOldGCTasks                  bool                product             false
TraceSafepointCleanupTime                bool                product             false
BCEATraceLevel                           intx                product             0
MaxJavaStackTraceDepth                   intx                product             1024
ExtendedDTraceProbes                     bool                product             false
DTraceMethodProbes                       bool                product             false
DTraceAllocProbes                        bool                product             false
DTraceMonitorProbes                      bool                product             false
[INFO] Detach from vm.
```