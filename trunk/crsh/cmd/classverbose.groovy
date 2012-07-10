import java.lang.management.*;

class classverbose extends CRaSHCommand {
  @Usage("show or set class verbose")
  @Command
  Object main(@Usage("get or set class verbose") @Option(names=["v","verbose"]) String verbose) {
    if (verbose != null) {
        ManagementFactory.getClassLoadingMXBean().setVerbose(Boolean.parseBoolean(verbose));
    }
    return ManagementFactory.getClassLoadingMXBean().isVerbose();
  }
}
