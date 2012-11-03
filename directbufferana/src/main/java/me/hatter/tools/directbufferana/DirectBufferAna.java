package me.hatter.tools.directbufferana;

import java.io.PrintStream;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import sun.jvm.hotspot.oops.DefaultHeapVisitor;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.IntField;
import sun.jvm.hotspot.oops.LongField;
import sun.jvm.hotspot.oops.ObjectHeap;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.utilities.SystemDictionaryHelper;

// Some code copied from: https://gist.github.com/1593521
public class DirectBufferAna {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }
        DirectBufferAnaTool.main(UnixArgsutil.ARGS.args(), System.out);
    }

    public static class DirectBufferAnaTool extends Tool {

        public static void main(String[] args, PrintStream err) {
            DirectBufferAnaTool ps = new DirectBufferAnaTool();
            ps.start(args, err);
            ps.stop();
        }

        public void run() {
            long reservedMemory = getStaticLongFieldValue("java.nio.Bits", "reservedMemory");
            long directMemory = getStaticLongFieldValue("sun.misc.VM", "directMemory");

            final long pageSize = getStaticIntFieldValue("java.nio.Bits", "pageSize");
            ObjectHeap heap = VM.getVM().getObjectHeap();
            InstanceKlass deallocatorKlass = SystemDictionaryHelper.findInstanceKlass("java.nio.DirectByteBuffer$Deallocator");
            final LongField addressField = (LongField) deallocatorKlass.findField("address", "J");
            final IntField capacityField = (IntField) deallocatorKlass.findField("capacity", "I");
            final int[] countHolder = new int[1];

            heap.iterateObjectsOfKlass(new DefaultHeapVisitor() {

                public boolean doObj(Oop oop) {
                    long address = addressField.getValue(oop);
                    if (address == 0) return false; // this deallocator has already been run

                    long capacity = capacityField.getValue(oop);
                    long mallocSize = capacity + pageSize;
                    countHolder[0]++;

                    if (UnixArgsutil.ARGS.flags().contains("verbose")) {
                        System.out.printf("  0x%016x: capacity = %f MB (%d bytes),"
                                                  + " mallocSize = %f MB (%d bytes)\n", address, toM(capacity),
                                          capacity,
                                          toM(mallocSize), mallocSize);
                    }

                    return false;
                }
            }, deallocatorKlass, false);

            System.out.println("NIO direct memory: (in bytes)");
            System.out.printf("  reserved size = %f MB (%d bytes)\n", toM(reservedMemory), reservedMemory);
            System.out.printf("  max size      = %f MB (%d bytes)\n", toM(directMemory), directMemory);
            long totalMallocSize = reservedMemory + pageSize * countHolder[0];
            System.out.printf("  malloc'd size = %f MB (%d bytes)\n", toM(totalMallocSize), totalMallocSize);
        }

        public static double toM(long value) {
            return value / (1024 * 1024.0);
        }

        public static long getStaticLongFieldValue(String className, String fieldName) {
            InstanceKlass klass = SystemDictionaryHelper.findInstanceKlass(className);
            LongField field = (LongField) klass.findField(fieldName, "J");
            return field.getValue(klass);
        }

        public static int getStaticIntFieldValue(String className, String fieldName) {
            InstanceKlass klass = SystemDictionaryHelper.findInstanceKlass(className);
            IntField field = (IntField) klass.findField(fieldName, "I");
            return field.getValue(klass);
        }
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar directbufferana.jar <PID>");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
