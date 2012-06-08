import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class ClassLoaderDefine {

    @OnMethod(clazz = "java.lang.ClassLoader", method = "defineClass")
    public static void onClassLoaderDefine() {
        print("=== java.lang.ClassLoader#defineClass ===");
        jstack(3);
    }
}
