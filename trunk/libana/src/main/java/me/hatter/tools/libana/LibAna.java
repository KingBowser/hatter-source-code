package me.hatter.tools.libana;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.JavaWalkTool;
import me.hatter.tools.commons.file.JavaWalkTool.AbstractClassJarJavaWalker;
import me.hatter.tools.commons.file.JavaWalkTool.AcceptType;

public class LibAna {

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.flags().containsAny("h", "help")) {
            usage();
        }

        String dir = Environment.USER_DIR;
        if (UnixArgsutil.ARGS.args().length > 0) {
            dir = UnixArgsutil.ARGS.args()[0];
        }

        final boolean isTrace = !UnixArgsutil.ARGS.flags().contains("notrace");
        final boolean isVerbose = !UnixArgsutil.ARGS.flags().contains("verbose");
        final AtomicLong processedCount = new AtomicLong(0);

        JavaWalkTool tool = new JavaWalkTool(new File(dir));
        tool.walk(new AbstractClassJarJavaWalker() {

            public void readInputStream(InputStream is, File file, String name, AcceptType type) {
                processedCount.incrementAndGet();
                if (isVerbose) {
                    if (type == AcceptType.File) {
                        System.out.println("Read file: " + file.getPath());
                    }
                    if (type == AcceptType.Entry) {
                        System.out.println("Read entry: " + file.getPath() + "!" + name);
                    }
                } else if (isTrace && ((processedCount.get() % 100) == 0)) {
                    System.out.print(".");
                }
                // TODO Auto-generated method stub
            }
        });
    }

    private static void usage() {
        System.out.println("Usage:");
        System.exit(0);
    }
}
