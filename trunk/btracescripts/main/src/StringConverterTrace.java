import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class StringConverterTrace {

    @OnMethod(clazz = "com.thoughtworks.xstream.converters.basic.StringConverter",
              method = "fromString")
    public static void onFromString() {
        println("=== com.thoughtworks.xstream.converters.basic.StringConverter." +
        		"fromString ===");
        jstack(10);
    }
}
