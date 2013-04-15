package me.hatter.tools.commons.encoding;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.hatter.tools.commons.lang.ReferenceObject;

public class EncodingDetectUtil {

    public static String detectString(byte[] bytes, ReferenceObject<String> outEncoding, String defaultEncoding,
                                      String... encodings) {
        try {
            String defaultCode = new String(bytes, defaultEncoding);

            int defaultMessyCodeCount = MessyCodeDetector.countTestMessyCode(defaultCode);

            if (defaultMessyCodeCount == 0) {
                if (outEncoding != null) {
                    outEncoding.setValue(defaultEncoding);
                }
                return defaultCode;
            }

            Map<String, Integer> messyCodeCountMap = new HashMap<String, Integer>();
            messyCodeCountMap.put(defaultEncoding, defaultMessyCodeCount);

            for (String enc : encodings) {
                String code = new String(bytes, enc);
                int encMessyCodeCount = MessyCodeDetector.countTestMessyCode(code);
                if (encMessyCodeCount == 0) {
                    if (outEncoding != null) {
                        outEncoding.setValue(enc);
                    }
                    return code;
                } else {
                    messyCodeCountMap.put(enc, encMessyCodeCount);
                }
            }

            String minMessyCodeEnc = null;
            int minMessyCodeCount = Integer.MAX_VALUE;
            for (Entry<String, Integer> entry : messyCodeCountMap.entrySet()) {
                if (entry.getValue().intValue() < minMessyCodeCount) {
                    minMessyCodeEnc = entry.getKey();
                }
            }

            if (outEncoding != null) {
                outEncoding.setValue(minMessyCodeEnc);
            }
            return new String(bytes, minMessyCodeEnc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
