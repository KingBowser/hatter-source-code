package sun.management;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sun.management.VMOption;
import com.sun.management.VMOption.Origin;

public class FlagAgent {

    static {
        // INIT sun.management.Flag
        ManagementFactory.getDiagnosticMXBean().getDiagnosticOptions();
    }

    private String       name;
    private Object       value;
    private Origin       origin;
    private boolean      writeable;
    private boolean      external;

    private static Field fName;
    private static Field fOrigin;
    static {
        try {
            fName = Flag.class.getDeclaredField("name");
            fOrigin = Flag.class.getDeclaredField("origin");
            fName.setAccessible(true);
            fOrigin.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FlagAgent(Flag flag) {
        try {
            this.name = (String) fName.get(flag);
            this.value = flag.getValue();
            this.origin = (Origin) fOrigin.get(flag);
            this.writeable = flag.isWriteable();
            this.external = flag.isExternal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FlagAgent(String name, Object value, boolean writeable, boolean external, Origin origin) {
        this.name = name;
        this.value = value;
        this.origin = origin;
        this.writeable = writeable;
        this.external = external;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public Origin getOrigin() {
        return origin;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public boolean isExternal() {
        return external;
    }

    public VMOption getVMOption() {
        return new VMOption(name, (value == null) ? null : value.toString(), writeable, origin);
    }

    public static FlagAgent getFlag(String name) {
        return new FlagAgent(Flag.getFlag(name));
    }

    public static List<FlagAgent> getAllFlags() {
        List<FlagAgent> allFlagAgents = new ArrayList<FlagAgent>();
        List<Flag> allFlags = Flag.getAllFlags();
        for (Flag flag : allFlags) {
            allFlagAgents.add(new FlagAgent(flag));
        }
        return allFlagAgents;
    }

    public static synchronized void setLongValue(String name, long value) {
        Flag.setLongValue(name, value);
    }

    public static synchronized void setBooleanValue(String name, boolean value) {
        Flag.setBooleanValue(name, value);
    }

    public static synchronized void setStringValue(String name, String value) {
        Flag.setStringValue(name, value);
    }

}
