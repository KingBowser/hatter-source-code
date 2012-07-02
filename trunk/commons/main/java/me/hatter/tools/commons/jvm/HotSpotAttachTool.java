package me.hatter.tools.commons.jvm;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.log.LogUtil;

import com.sun.tools.attach.VirtualMachine;

@SuppressWarnings("restriction")
public class HotSpotAttachTool {

    private String                                pid;
    private final AtomicReference<VirtualMachine> refVM = new AtomicReference<VirtualMachine>(null);

    public HotSpotAttachTool(String pid) {
        this.pid = pid;
    }

    public VirtualMachine getVM() {
        return refVM.get();
    }

    public void attach() {
        try {
            LogUtil.info("Attach to vm: " + pid);
            refVM.set(VirtualMachine.attach(pid));
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    detach();
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void detach() {
        synchronized (refVM) {
            if (refVM.get() != null) {
                VirtualMachine vm = refVM.get();
                refVM.set(null);
                try {
                    LogUtil.info("Detach from vm.");
                    vm.detach();
                } catch (IOException e) {
                    LogUtil.error("Detach from vm failed: " + ExceptionUtil.printStackTrace(e));
                }
            }
        }
    }
}
