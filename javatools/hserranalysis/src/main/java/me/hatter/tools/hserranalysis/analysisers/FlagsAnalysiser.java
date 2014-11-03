package me.hatter.tools.hserranalysis.analysisers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;

/**
 * @see http://en.wikipedia.org/wiki/FLAGS_register
 * @author hatterjiang
 */
public class FlagsAnalysiser {

    public static void main(String[] args) {
        new FlagsAnalysiser().analysis();
    }

    public static enum Category {
        Status, System, Control
    }

    public static class EFlag {

        public String   name;
        public String   abbrName;
        public Category category;
        public String   descrition;

        public EFlag(String name, String abbrName, Category category, String descrition) {
            this.name = name;
            this.abbrName = abbrName;
            this.category = category;
            this.descrition = descrition;
        }
    }

    private static Map<Integer, EFlag> bitFlagMap = new HashMap<Integer, EFlag>();
    static {
        // FLAGS
        bitFlagMap.put(0, new EFlag("Carry flag", "CF", Category.Status, null));
        bitFlagMap.put(1, new EFlag("Reserved", "1", null, null));
        bitFlagMap.put(2, new EFlag("Parity flag", "PF", Category.Status, null));
        bitFlagMap.put(3, new EFlag("Reserved", "0", null, null));
        bitFlagMap.put(4, new EFlag("Adjust flag", "AF", Category.Status, null));
        bitFlagMap.put(5, new EFlag("Reserved", "0", null, null));
        bitFlagMap.put(6, new EFlag("Zero flag", "ZF", Category.Status, null));
        bitFlagMap.put(7, new EFlag("Sign flag", "SF", Category.Status, null));
        bitFlagMap.put(8, new EFlag("Trap flag", "TF", Category.System, "(single step)"));
        bitFlagMap.put(9, new EFlag("Interrupt enable flag", "IF", Category.Control, null));
        bitFlagMap.put(10, new EFlag("Direction flag", "DF", Category.Control, null));
        bitFlagMap.put(11, new EFlag("Overflow flag", "OF", Category.Status, null));
        bitFlagMap.put(12, new EFlag("I/O privilege level", "IOPL", Category.System,
                                     "(286+ only), always 1 on 8086 and 186"));
        bitFlagMap.put(13, new EFlag("I/O privilege level", "IOPL", Category.System,
                                     "(286+ only), always 1 on 8086 and 186"));
        bitFlagMap.put(14,
                       new EFlag("Nested task flag", "NT", Category.System, "(286+ only), always 1 on 8086 and 186"));
        bitFlagMap.put(15, new EFlag("Reserved", "0", null, "always 1 on 8086 and 186, always 0 on later models"));
        // EFLAGS
        bitFlagMap.put(16, new EFlag("Resume flag", "RF", Category.System, "(386+ only)"));
        bitFlagMap.put(17, new EFlag("Virtual 8086 mode flag", "VM", Category.System, "(386+ only)"));
        bitFlagMap.put(18, new EFlag("Alignment check", "AC", Category.System, "(486SX+ only)"));
        bitFlagMap.put(19, new EFlag("Virtual interrupt flag", "VIF", Category.System, "(Pentium+)"));
        bitFlagMap.put(20, new EFlag("Virtual interrupt pending", "VIP", Category.System, "(Pentium+)"));
        bitFlagMap.put(21, new EFlag("Able to use CPUID instruction", "ID", Category.System, "(Pentium+)"));
        for (int i = 22; i < 64; i++) {
            bitFlagMap.put(i, new EFlag("Reserved", "0", null, null));
        }
        // 32-63 RFLAGS
    }

    public void analysis() {
        System.out.println(FlagsAnalysiser.class.getSimpleName() + " :");

        String line;
        System.out.print("> ");
        while ((line = System.console().readLine()) != null) {
            if (Arrays.asList("exit", "quit", "bye").contains(StringUtil.trim(StringUtil.toLowerCase(line)))) {
                System.out.println("Exit!");
                System.exit(0);
            }
            line = StringUtil.trimToEmpty(line);
            if (!line.isEmpty()) {
                List<String> out = disassemble(line);
                if (CollectionUtil.isNotEmpty(out)) {
                    for (String o : out) {
                        System.out.println(" " + o);
                    }
                }
            }
            System.out.print("> ");
        }
    }

    private List<String> disassemble(String input) {
        List<String> out = new ArrayList<String>();
        out.add(StringUtil.paddingSpaceRight("IDX", 3) + " " + StringUtil.paddingSpaceRight("NAME", 35) + " "
                + StringUtil.paddingSpaceRight("ABBR", 5) + " " + StringUtil.paddingSpaceRight("VAL", 3) + " "
                + StringUtil.paddingSpaceRight("CATE", 8) + " " + "DESCRIPTION");
        long eflag;
        try {
            eflag = Long.parseLong((input.startsWith("0x") ? input.substring(2) : input), 16);
        } catch (Exception e) {
            LogUtil.error("Format error: " + input + "; " + e.getMessage());
            return null;
        }
        for (int i = 0; i < 64; i++) {
            EFlag ef = bitFlagMap.get(i);
            boolean f = ((eflag & (1L << i)) != 0);
            out.add(StringUtil.paddingSpaceRight(String.valueOf(i), 3) + " "
                    + StringUtil.paddingSpaceRight(ef.name, 35) + " " + StringUtil.paddingSpaceRight(ef.abbrName, 5)
                    + " " + StringUtil.paddingSpaceRight((f ? "1" : "0"), 3) + " "
                    + StringUtil.paddingSpaceRight((ef.category == null) ? "" : ef.category.name(), 8) + " "
                    + StringUtil.notNull(ef.descrition));
        }
        return out;
    }
}
