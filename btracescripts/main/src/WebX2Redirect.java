import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class WebX2Redirect {

    @OnMethod(clazz = "com.alibaba.service.rundata.DefaultRunData", method = "setRedirectLocation")
    public static void onRedirect() {
        println("=== com.alibaba.service.rundata.DefaultRunData.setRedirectLocation ===");
        jstack(20);
    }
}
