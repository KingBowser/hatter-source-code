class classlocate extends CRaSHCommand {
  @Usage("class locate")
  @Command
  Object main(@Usage("class name") @Option(names=["c","class"]) String clazz) {
    if (clazz != null) {
      def cl = sun.misc.Launcher.getLauncher().loader;
      def cln = clazz.replace('.', '/') + ".class";
      def u = cl.findResource(cln);
      def result = "";
      result += "App:" + u + "\n";
      u = cl.getParent().findResource(cln);
      result += "Exp:" + u + "\n";
      u = ClassLoader.getBootstrapResource(cln);
      result += "Boot" + u + "\n";
      return result;
    }
  }
}

