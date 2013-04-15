package me.hatter.tools.commons.encoding;

import java.lang.Character.UnicodeBlock;
import java.util.HashSet;
import java.util.Set;

public class MessyCodeDetector {

    private static final Set<UnicodeBlock> normalBlocks = new HashSet<UnicodeBlock>();
    static {
        normalBlocks.add(UnicodeBlock.BASIC_LATIN);
        normalBlocks.add(UnicodeBlock.LATIN_1_SUPPLEMENT);
        normalBlocks.add(UnicodeBlock.ARROWS);
        normalBlocks.add(UnicodeBlock.GREEK);
        normalBlocks.add(UnicodeBlock.MATHEMATICAL_OPERATORS);
        normalBlocks.add(UnicodeBlock.LETTERLIKE_SYMBOLS);
        normalBlocks.add(UnicodeBlock.GENERAL_PUNCTUATION);
        normalBlocks.add(UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
        normalBlocks.add(UnicodeBlock.CJK_COMPATIBILITY);
        normalBlocks.add(UnicodeBlock.CJK_COMPATIBILITY_FORMS);
        normalBlocks.add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
        normalBlocks.add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
        normalBlocks.add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
        normalBlocks.add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
        normalBlocks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
        normalBlocks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
        normalBlocks.add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
    }

    public static int countTestMessyCode(String text) {
        if (text == null) {
            return 0;
        }
        int messyCodeCount = 0;
        int len = text.length();
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            UnicodeBlock ub = UnicodeBlock.of(c);
            if (!normalBlocks.contains(ub)) {
                messyCodeCount++;
            }
        }
        return messyCodeCount;
    }

}
