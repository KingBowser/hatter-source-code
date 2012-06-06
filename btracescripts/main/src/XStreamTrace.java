import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class XStreamTrace {

    @OnMethod(clazz = "com.thoughtworks.xstream.XStream", method = "<init>")
    public static void onNewXStream() {
        print("=== new xstream ===");
        jstack(3);
    }
}
