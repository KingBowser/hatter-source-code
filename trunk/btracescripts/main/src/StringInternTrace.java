import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class StringInternTrace {

    @OnMethod(clazz = "/.*/", method = "/.*/",
              location = @Location(value = Kind.CALL, clazz = "java.lang.String", method = "intern"))
    public static void m(@ProbeClassName String pcm, @ProbeMethodName String probeMethod,
                         @TargetInstance Object instance) {
        println(strcat(pcm, strcat("#", probeMethod)));
        println(strcat(">>>> ", str(instance)));
    }
}
