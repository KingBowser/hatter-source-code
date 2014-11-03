package me.hatter.tools.hserranalysis;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.util.VersionBuildUtil;
import me.hatter.tools.hserranalysis.analysisers.ELFFileAnalysiser;
import me.hatter.tools.hserranalysis.analysisers.FlagsAnalysiser;
import me.hatter.tools.hserranalysis.analysisers.X86DisassemblerAnalysiser;

public class AnalysiserMain {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        UnixArgsutil.parseGlobalArgs(args);

        boolean printUsage = true;

        if (UnixArgsutil.ARGS.args().length > 0) {
            printUsage = false;

            if ("x86disassemble".equals(UnixArgsutil.ARGS.args()[0])) {
                (new X86DisassemblerAnalysiser()).analysis();
            } else if ("flags".equals(UnixArgsutil.ARGS.args()[0])) {
                (new FlagsAnalysiser()).analysis();
            } else if ("elffile".equals(UnixArgsutil.ARGS.args()[0])) {
                (new ELFFileAnalysiser()).analysis();
            } else {
                printUsage = true;
            }
        }

        if (printUsage) {
            printUsage();
        }
        System.exit(0);
    }

    private static void printUsage() {
        System.out.println("Usage[b" + VersionBuildUtil.getVersionBuild() + "]:");
        System.out.println("  java -jar hserranalysisall.jar [options] <command>");
        System.out.println("  Commands:");
        System.out.println("    x86disassemble              Disassembler");
        System.out.println("    flags                       Flags");
        System.out.println("    elffile [options] <file>    ELFFile");
        System.out.println("      --v                       Verbose(--verbose)");
        System.out.println();
    }
}
