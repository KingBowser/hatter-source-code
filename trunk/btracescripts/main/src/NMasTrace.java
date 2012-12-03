import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Self;

@BTrace
public class NMasTrace {

    @OnMethod(clazz = "com.alibaba.intl.nmas.biz.execute.NmasTaskExecutor", method = "execute")
    public static void m0(@Self Object self) {
        println(strcat("\n==== com.alibaba.intl.nmas.biz.execute.NmasTaskExecutor#execute ====\n\t",
                       strcat(str(get(field(classOf(self), "taskName"), self)), strcat("  ", Time.timestamp("yyyy/MM/dd HH:mm:ss")))));
    }

    @OnMethod(clazz = "com.alibaba.intl.nmas.biz.execute.NmasTaskEntry", method = "execute")
    public static void m1(@Self Object self) {
        println(strcat("\n==== com.alibaba.intl.nmas.biz.execute.NmasTaskEntry#execute ====\n\t",
                       strcat(str(get(field(classOf(self), "taskName"), self)), strcat("  ", Time.timestamp("yyyy/MM/dd HH:mm:ss")))));
    }

    @OnMethod(clazz = "com.alibaba.intl.nmas.biz.execute.task.monitor.P4pNmasExtMatchTaskAsynMonitor", method = "execute")
    public static void m2() {
        println("\n==== com.alibaba.intl.nmas.biz.execute.task.monitor.P4pNmasExtMatchTaskAsynMonitor#execute ====");
    }

    @OnMethod(clazz = "com.alibaba.intl.nmas.biz.execute.task.monitor.P4pNmasTaskTimeOutCountAsynMonitor", method = "execute")
    public static void m3() {
        println("\n==== com.alibaba.intl.nmas.biz.execute.task.monitor.P4pNmasTaskTimeOutCountAsynMonitor#execute ====");
    }
}
