package me.hatter.tools.hserranalysis.analysisers;

import java.io.File;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;
import sun.jvm.hotspot.debugger.posix.elf.ELFFile;
import sun.jvm.hotspot.debugger.posix.elf.ELFFileParser;
import sun.jvm.hotspot.debugger.posix.elf.ELFHeader;
import sun.jvm.hotspot.debugger.posix.elf.ELFSectionHeader;
import sun.jvm.hotspot.debugger.posix.elf.ELFStringTable;
import sun.jvm.hotspot.debugger.posix.elf.ELFSymbol;

public class ELFFileAnalysiser {

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(new String[] { "elffile", "/Users/hatterjiang/java_ssh/proxy" });
        (new ELFFileAnalysiser()).analysis();
    }

    public void analysis() {
        System.out.println(ELFFileAnalysiser.class.getSimpleName() + " :");
        if (UnixArgsutil.ARGS.args().length < 2) {
            LogUtil.warn("ELF file is not assigned!");
            return;
        }
        String f = UnixArgsutil.ARGS.args()[1];
        if (!(new File(f)).exists()) {
            LogUtil.error("ELF file not found: " + f);
            return;
        }
        ELFFile elfFile = ELFFileParser.getParser().parse(f);

        ELFHeader elfHeader = elfFile.getHeader();
        System.out.println("File: " + f);
        System.out.println("Object size: " + getObjectSize(elfFile.getObjectSize()));
        System.out.println("Data encoding: " + getDataEncoding(elfFile.getEncoding()));
        System.out.println("Arch: " + getArch(elfHeader.getArch()));
        System.out.println("File type: " + getFileType(elfHeader.getFileType()));

        if (!UnixArgsutil.ARGS.flags().containsAny("v", "verbose")) {
            return;
        }

        final String IN_S = "    ";
        int depth = 0;
        int h = elfHeader.getNumberOfSectionHeaders();
        System.out.println(StringUtil.repeat(IN_S, depth) + "--> Start: reading " + h + " section headers.");
        for (int i = 0; i < elfHeader.getNumberOfSectionHeaders(); i++) {
            depth++;
            ELFSectionHeader sh = elfHeader.getSectionHeader(i);
            String str = sh.getName();
            System.out.println(StringUtil.repeat(IN_S, depth) + "--> Start: Section (" + i + ") " + str);

            int num = 0;
            if ((num = sh.getNumberOfSymbols()) != 0) {
                depth++;
                System.out.println(StringUtil.repeat(IN_S, depth) + "--> Start: reading " + num + " symbols.");
                for (int j = 0; j < num; j++) {
                    depth++;
                    ELFSymbol sym = sh.getELFSymbol(j);
                    String name = sym.getName();
                    if (name != null) {
                        System.out.println(StringUtil.repeat(IN_S, depth) + name);
                    }
                    depth--;
                }
                System.out.println(StringUtil.repeat(IN_S, depth) + "<-- End: reading " + num + " symbols.");
                depth--;
            }
            ELFStringTable st;
            if (sh.getType() == ELFSectionHeader.TYPE_STRTBL) {
                depth++;
                System.out.println(StringUtil.repeat(IN_S, depth) + "--> Start: reading string table.");
                st = sh.getStringTable();
                if (st != null) {
                    // int stn = st.getNumStrings();
                    // for (int j = 0; j < stn; j++) {
                    // System.out.println(StringUtil.repeat(IN_S, depth) + st.get(j));
                    // }
                }
                System.out.println(StringUtil.repeat(IN_S, depth) + "<-- End: reading string table.");
                depth--;
            }
            if (sh.getType() == ELFSectionHeader.TYPE_HASH) {
                depth++;
                System.out.println(StringUtil.repeat(IN_S, depth) + "--> Start: reading hash table.");
                sh.getHashTable();
                System.out.println(StringUtil.repeat(IN_S, depth) + "<-- End: reading hash table.");
                depth--;
            }
            System.out.println(StringUtil.repeat(IN_S, depth) + "<-- End: Section (" + i + ") " + str);
            depth--;
        }
        System.out.println(StringUtil.repeat(IN_S, depth) + "<-- End: reading " + h + " section headers.");

        elfFile.close();
    }

    static String getObjectSize(byte os) {
        return (os == 0) ? "Invalid Object Size" : (os == 1) ? "32-bit" : "64-bit";
    }

    static String getDataEncoding(byte e) {
        return (e == 0) ? "Invalid Data Encoding" : (e == 1) ? "LSB" : "MSB";
    }

    static String getArch(short a) {
        // /** No architecture type. */
        // public static final int ARCH_NONE = 0;
        // /** AT&T architecture type. */
        // public static final int ARCH_ATT = 1;
        // /** SPARC architecture type. */
        // public static final int ARCH_SPARC = 2;
        // /** Intel 386 architecture type. */
        // public static final int ARCH_i386 = 3;
        // /** Motorolla 68000 architecture type. */
        // public static final int ARCH_68k = 4;
        // /** Motorolla 88000 architecture type. */
        // public static final int ARCH_88k = 5;
        // /** Intel 860 architecture type. */
        // public static final int ARCH_i860 = 7;
        // /** MIPS architecture type. */
        // public static final int ARCH_MIPS = 8;
        switch (a) {
            case 1:
                return "att";
            case 2:
                return "sparc";
            case 3:
                return "i386";
            case 4:
                return "68k";
            case 5:
                return "88k";
            case 7:
                return "i860";
            case 8:
                return "mips";
        }
        return "Invalid Arch";
    }

    static String getFileType(short ft) {
        // /** No file type. */
        // public static final int FT_NONE = 0;
        // /** Relocatable file type. */
        // public static final int FT_REL = 1;
        // /** Executable file type. */
        // public static final int FT_EXEC = 2;
        // /** Shared object file type. */
        // public static final int FT_DYN = 3;
        // /** Core file file type. */
        // public static final int FT_CORE = 4;
        // /** Processor specific. */
        // public static final int FT_LOCPROC = 0xff00;
        // /** Processor specific. */
        // public static final int FT_HICPROC = 0xffff;
        switch (ft) {
            case 1:
                return "rel";
            case 2:
                return "exec";
            case 3:
                return "dyn";
            case 4:
                return "core";
        }
        return "Invalid File Type";
    }
}
