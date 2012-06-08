import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class SystemGC {

    @OnTimer(10000)
    public static void onTimer() {
        gc();
    }
}
