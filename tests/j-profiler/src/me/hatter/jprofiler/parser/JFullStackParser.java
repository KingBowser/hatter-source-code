package me.hatter.jprofiler.parser;

import java.util.ArrayList;
import java.util.List;

import me.hatter.jprofiler.object.JFullStack;
import me.hatter.jprofiler.object.JStack;

public class JFullStackParser {

    public static JFullStack parse(List<List<String>> linesList) {
        JFullStack jFullStack = new JFullStack();
        jFullStack.setjStackList(new ArrayList<JStack>());

        for (List<String> lines : linesList) {
            JStack jStack = JStackParser.parse(lines);
            if (jStack != null) {
                jFullStack.getjStackList().add(jStack);
            }
        }

        return jFullStack;
    }
}
