package me.hatter.tools.hserranalysis.analysisers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.hserranalysis.sun.jvm.hotspot.asm.CPUHelper;
import me.hatter.tools.hserranalysis.sun.jvm.hotspot.asm.Disassembler;
import me.hatter.tools.hserranalysis.sun.jvm.hotspot.asm.Instruction;
import me.hatter.tools.hserranalysis.sun.jvm.hotspot.asm.InstructionVisitor;
import me.hatter.tools.hserranalysis.sun.jvm.hotspot.asm.SymbolFinder;
import me.hatter.tools.hserranalysis.sun.jvm.hotspot.asm.x86.X86Helper;

/**
 * @see http://rednaxelafx.iteye.com/blog/729214
 * @author hatterjiang
 */
public class X86DisassemblerAnalysiser {

    public static void main(String[] args) {
        new X86DisassemblerAnalysiser().analysis();
    }

    public void analysis() {
        System.out.println(X86DisassemblerAnalysiser.class.getSimpleName() + " :");

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
        CPUHelper cpuHelper = new X86Helper();

        String[] addrAndCode = input.split(":");
        if (addrAndCode.length != 2) {
            LogUtil.error("Format error: " + input);
            return null;
        }

        long startPc;
        byte[] code;
        String addr;

        String[] codes = new String[] {};
        try {
            codes = addrAndCode[1].trim().split(" ");

            addr = (addrAndCode[0].startsWith("0x") ? addrAndCode[0].substring(2) : addrAndCode[0]);
            startPc = Long.parseLong(addr, 16);
            code = new byte[codes.length];
            for (int i = 0; i < codes.length; i++) {
                code[i] = (byte) Integer.parseInt(codes[i], 16);
            }
        } catch (Exception e) {
            LogUtil.error("Format error: " + input + "; " + e.getMessage());
            LogUtil.error("Codes: " + Arrays.asList(codes));
            return null;
        }

        Disassembler dasm = cpuHelper.createDisassembler(startPc, code);

        List<String> out = new ArrayList<String>();
        dasm.decode(new RawCodeVisitor(out, addr.length()));

        return out;
    }

    private static class RawCodeVisitor implements InstructionVisitor {

        private final int          addrLen;
        private final List<String> out;
        private final SymbolFinder symFinder = new DummySymbolFinder();

        public RawCodeVisitor(List<String> out, int addrLen) {
            this.addrLen = addrLen;
            this.out = out;
        }

        public void prologue() {
            // do nothing
        }

        public void epilogue() {
            // do nothing
        }

        public void visit(long currentPc, Instruction instr) {
            out.add("0x" + StringUtil.repeat("0", (addrLen - Long.toHexString(currentPc).length()))
                    + Long.toHexString(currentPc) + ":  " + instr.asString(currentPc, symFinder));
        }

    }

    public static class DummySymbolFinder implements SymbolFinder {

        public String getSymbolFor(long address) {
            return "0x" + Long.toHexString(address);
        }
    }
}
