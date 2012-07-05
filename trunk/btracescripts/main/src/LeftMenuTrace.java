import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.AnyType;

@BTrace
public class LeftMenuTrace {

    @OnMethod(clazz = "com.alibaba.intl.menuservice.client.MenuClient", method = "getRenderdHeadMenu")
    public static void m1(AnyType[] args) {
        printArray(args);
        if ((args != null) && (args.length == 2) && (str(args[0]) != null) && (indexOf(str(args[0]), "photobank") >= 0)) {
            println("=== com.alibaba.intl.menuservice.client.MenuClient.getRenderdSubMenu ===");
            jstack();
        }
    }
}
