import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class StringIntern {

    @OnMethod(clazz = "java.lang.String", method = "intern")
    public static void onStringIntern() {
        print("=== java.lang.String#intern ===");
        jstack(3);
    }
}
