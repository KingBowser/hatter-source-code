package me.hatter.tests.diff;

import java.util.List;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;
import difflib.ChangeDelta;
import difflib.DeleteDelta;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.InsertDelta;
import difflib.Patch;

public class DiffTest2 {

    public static void main(String[] args) {
        List<String> l1 = IOUtil.readToList(IOUtil.readResourceToString(DiffTest2.class, "/l1.txt"));
        List<String> l2 = IOUtil.readToList(IOUtil.readResourceToString(DiffTest2.class, "/l2.txt"));

        Patch p = DiffUtils.diff(l1, l2);

        System.out.println(StringUtil.join(p.getDeltas().toArray(), "\n"));
        System.out.println();
        for (Delta d : p.getDeltas()) {
            if (d instanceof ChangeDelta) {
                ChangeDelta cd = (ChangeDelta) d;
                int opos = cd.getOriginal().getPosition();
                for (Object line : cd.getOriginal().getLines()) {
                    System.out.println("[" + (opos++) + "]" + "- " + line);
                }
                int rpos = cd.getRevised().getPosition();
                for (Object line : cd.getRevised().getLines()) {
                    System.out.println("[" + (rpos++) + "]" + "+ " + line);
                }
            }
            if (d instanceof InsertDelta) {
                InsertDelta id = (InsertDelta) d;
                int rpos = id.getRevised().getPosition();
                for (Object line : id.getRevised().getLines()) {
                    System.out.println("[" + (rpos++) + "]" + "+ " + line);
                }
            }
            if (d instanceof DeleteDelta) {
                DeleteDelta dd = (DeleteDelta) d;
                int rpos = dd.getRevised().getPosition();
                for (Object line : dd.getRevised().getLines()) {
                    System.out.println("[" + (rpos++) + "]" + "- " + line);
                }
            }
        }
    }
}
