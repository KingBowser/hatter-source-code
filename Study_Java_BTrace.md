## 概述 ##
官方网站：http://kenai.com/projects/btrace

升级版BTrace2：http://kenai.com/projects/btrace/sources/btrace2/show

BTrace is a safe, dynamic tracing tool for the Java platform. BTrace can be used to dynamically trace a running Java program (similar to DTrace for OpenSolaris applications and OS). BTrace dynamically instruments the classes of the target application to inject tracing code ("bytecode tracing"). Tracing code is expressed in Java programming language. There is also integration with DTrace for the OpenSolaris platform.

## 原理介绍 ##
[java.lang.instrument.Instrumentation](http://docs.oracle.com/javase/6/docs/technotes/guides/instrumentation/index.html)

## 命令行 ##
```
btrace [-I <include-path>] [-p <port>] [-cp <classpath>] <pid> <btrace-script> [<args>]
```

## 参数 ##
```
-Dcom.sun.btrace.trackRetransforms=false
```

| **参数** | **默认值** | **说明** |
|:-----------|:--------------|:-----------|
| `bootClassPath` |  | boot classpath to be used |
| `systemClassPath` |  | system classpath to be used |
| `debug` | `false` | turns on verbose debug messages (true/false) |
| `unsafe` | `false` | do not check for btrace restrictions violations (true/false) |
| `dumpClasses` | `false` | dump the transformed bytecode to files (true/false) |
| `dumpDir` | `.` | specifies the folder where the transformed classes will be dumped to |
| `stdout` |  | redirect the btrace output to stdout instead of writing it to an arbitrary file (true/false) |
| `probeDescPath` | `.` | the path to search for probe descriptor XMLs |
| `script` |  | the path to a script to be run at the agent startup |
| `scriptdir` |  | the path to a directory containing scripts to be run at the agent startup |
| `scriptOutputFile` |  | the path to a file the btrace agent will store its output |

在启动时指定btrace脚本 <sup>[4]</sup> ：
```
java -javaagent:btrace-agent.jar=script=<pre-compiled-btrace-script1>[,<pre-compiled-btrace-script1>]* 
      <MainClass> <AppArguments>
```

## 工具 ##
**生成简单BTrace的工具** http://hatter.me/js/btrace/

## 示例 ##
打印所有调用 `String.intern()` 的堆栈日志：
```
import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class Trace {

    @OnMethod(clazz = "/.*/", method = "/.*/", location = @Location(value = Kind.CALL, clazz = "java.lang.String", method = "intern"))
    public static void m0(@ProbeClassName String clazz, @ProbeMethodName String method, @TargetInstance Object instance) {
        println("\n==== java.lang.String#intern ====");
        jstack();
    }
}
```

# 使用 #

## Class Annotations ##
| `@com.sun.btrace.annotations.DTrace` | _和`dtrace`相关，似乎不能在Linux，Mac下使用 :'(_ |
|:-------------------------------------|:--------------------------------------------------------------|
| `@com.sun.btrace.annotations.DTraceRef` | _和`dtrace`相关，似乎不能在Linux，Mac下使用 :'(_ |
| `@com.sun.btrace.annotations.BTrace` | 标记类为BTrace脚本 |

## Field Annotations ##
| `@com.sun.btrace.annotations.Export` | 将变量导出到外部 |
|:-------------------------------------|:-------------------------|
| `@com.sun.btrace.annotations.Property` | 将变量导出为MXBeant属性 |
| `@com.sun.btrace.annotations.TLS` | 标记BTrace脚本的字段为线程安全 |

## Method Annotations ##
| `@com.sun.btrace.annotations.OnMethod` | 通过函数触发，如是参数调用点、函数调用开始、函数调用结束等 |
|:---------------------------------------|:----------------------------------------------------------------------------------------|
| `@com.sun.btrace.annotations.OnTimer` | 定时触发 |
| `@com.sun.btrace.annotations.OnError` | 异常时触发 |
| `@com.sun.btrace.annotations.OnExit` | BTrace退出时触发 |
| `@com.sun.btrace.annotations.OnEvent` | 事件触发，在BTrace运行时可通过`Ctrl+C`触发事件 |
| `@com.sun.btrace.annotations.OnLowMemory` | 低内存时触发 |
| `@com.sun.btrace.annotations.OnProbe` | `-` |

## Argument Annotations ##
| `@com.sun.btrace.annotations.Self` | this对象，当非static函数调用时的this对象 |
|:-----------------------------------|:------------------------------------------------------|
| `@com.sun.btrace.annotations.Return` | 返回值 |
| `@com.sun.btrace.annotations.ProbeClassName` | 类名 |
| `@com.sun.btrace.annotations.ProbeMethodName` | 方法名 |
| `@com.sun.btrace.annotations.Duration` | 调用时间 |
| `@com.sun.btrace.annotations.TargetInstance` | 目标对象 |
| `@com.sun.btrace.annotations.TargetMethodOrField` | 目标方法或字段 |

当出现未打标记的参数时会通过对应类型的切面传递参数，当 **`AnyType[]`** 指定时，则会传入剩余的全部参数，传递的参数则依赖于 **`Location`**：
| **`Kind.ENTRY, Kind.RETURN`** | the probed method arguments |
|:------------------------------|:----------------------------|
| **`Kind.THROW`** | the thrown exception |
| **`Kind.ARRAY_SET, Kind.ARRAY_GET`** | the array index |
| **`Kind.CATCH`** | the caught exception |
| **`Kind.FIELD_SET`** | the field value |
| **`Kind.LINE`** | the line number |
| **`Kind.NEW`** | the class name |
| **`Kind.ERROR`** | the thrown exception |

## 常用脚本 ##
| [ClassLoaderDefine.java](https://hatter-source-code.googlecode.com/svn/trunk/btracescripts/main/src/ClassLoaderDefine.java) | 打印类加载时的Stacktrace |
|:----------------------------------------------------------------------------------------------------------------------------|:--------------------------------|
| [StringInternTrace.java](https://hatter-source-code.googlecode.com/svn/trunk/btracescripts/main/src/StringInternTrace.java) | 打印调用`String.intern()`的Stacktrace |

### 参考资料 ###
`[1].` http://kenai.com/projects/btrace/pages/UserGuide<br>
<code>[2].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/guides/instrumentation/index.html'>http://docs.oracle.com/javase/6/docs/technotes/guides/instrumentation/index.html</a><br>
<code>[3].</code> <a href='http://kenai.com/projects/btrace/sources/btrace2/show'>http://kenai.com/projects/btrace/sources/btrace2/show</a><br>
<code>[4].</code> <a href='http://macrochen.iteye.com/blog/838920'>http://macrochen.iteye.com/blog/838920</a><br>