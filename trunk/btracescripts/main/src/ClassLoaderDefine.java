import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class ClassLoaderDefine {

    @SuppressWarnings("rawtypes")
    @OnMethod(clazz = "+java.lang.ClassLoader", method = "defineClass", location = @Location(Kind.RETURN))
    public static void onClassLoaderDefine(@Return Class cl) {
        println("=== java.lang.ClassLoader#defineClass ===");
        println(Strings.strcat("Loaded class: ", Reflective.name(cl)));
        jstack(10);
    }
}
