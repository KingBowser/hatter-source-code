import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class XStreamTrace {

    @OnMethod(clazz = "com.thoughtworks.xstream.XStream", method = "<init>")
    public static void onNewXStream() {
        println("=== new com.thoughtworks.xstream.XStream ===");
        jstack(10);
    }

    @OnMethod(clazz = "com.thoughtworks.xstream.XStream", method = "toXML")
    public static void onXStreamToXML() {
        println("=== com.thoughtworks.xstream.XStream#toXML ===");
        jstack(10);
    }

    @OnMethod(clazz = "com.thoughtworks.xstream.XStream", method = "fromXML")
    public static void onXStreamFromXML() {
        println("=== com.thoughtworks.xstream.XStream#fromXML ===");
        jstack(10);
    }
}
