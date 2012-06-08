import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class XStreamTrace {

    @OnMethod(clazz = "com.thoughtworks.xstream.XStream", method = "<init>")
    public static void onNewXStream() {
        print("=== new com.thoughtworks.xstream.XStream ===");
        jstack(3);
    }

    @OnMethod(clazz = "com.thoughtworks.xstream.XStream", method = "toXML")
    public static void onXStreamToXML() {
        print("=== com.thoughtworks.xstream.XStream#toXML ===");
        jstack(3);
    }

    @OnMethod(clazz = "com.thoughtworks.xstream.XStream", method = "fromXML")
    public static void onXStreamFromXML() {
        print("=== com.thoughtworks.xstream.XStream#fromXML ===");
        jstack(3);
    }
}
