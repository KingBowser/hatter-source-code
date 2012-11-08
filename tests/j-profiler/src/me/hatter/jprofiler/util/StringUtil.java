package me.hatter.jprofiler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static List<String> readLines(String string) {
        List<String> list = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new StringReader(string));
        try {
            for (String ln; ((ln = br.readLine()) != null);) {
                list.add(ln);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<List<String>> parseToListByEmptyLine(String string) {
        List<List<String>> listList = new ArrayList<List<String>>();
        List<String> partLines = new ArrayList<String>();
        List<String> stringLines = readLines(string);
        for (String ln : stringLines) {
            if (ln.trim().length() == 0) {
                if (partLines.size() > 0) {
                    listList.add(partLines);
                    partLines = new ArrayList<String>();
                }
            } else {
                partLines.add(ln);
            }
        }
        if (partLines.size() > 0) {
            listList.add(partLines);
        }
        return listList;
    }
}
