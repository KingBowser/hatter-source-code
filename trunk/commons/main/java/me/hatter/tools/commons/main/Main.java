package me.hatter.tools.commons.main;

import me.hatter.tools.commons.args.ArgsUtil;

/**
 * Sample:
 * 
 * <pre>
 * 
 * public class Sample extends Main {
 * 
 *     public static void main(String[] args) {
 *         (new Sample()).domain(args);
 *     }
 * 
 *     protected void invokemain(String[] args) {
 *         // XX your codes here
 *     }
 * }
 * </pre>
 * 
 * @author hatterjiang
 */
abstract public class Main {

    protected void domain(String[] args) {
        invokemain(ArgsUtil.processArgs(args));
    }

    abstract protected void invokemain(String[] args);
}
