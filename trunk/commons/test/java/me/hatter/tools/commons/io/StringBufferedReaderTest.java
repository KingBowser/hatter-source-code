package me.hatter.tools.commons.io;

import me.hatter.tools.commons.function.BiFunction;
import me.hatter.tools.commons.function.IndexedProcedure;

import org.junit.Test;

public class StringBufferedReaderTest {

    @Test
    public void testToIteratorTool() {
        String s = "a\nb\nc\r\nd\re";
        {
            StringBufferedReader sbr = new StringBufferedReader(s);
            sbr.toIteratorTool().each(new IndexedProcedure<String>() {

                @Override
                public void apply(String obj, int index) {
                    System.out.println(index + " : " + obj);
                }
            });
            IOUtil.closeQuietly(sbr);
        }
        {
            StringBufferedReader sbr = new StringBufferedReader(s);
            String r = sbr.toIteratorTool().reduce(new BiFunction<String, String, String>() {

                @Override
                public String apply(String objT, String objU) {
                    return objT + ":" + objU;
                }
            });
            System.out.println(r);
            IOUtil.closeQuietly(sbr);
        }
    }
}
