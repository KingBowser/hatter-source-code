package me.hatter.tools.commons.screen;

import me.hatter.tools.commons.color.Text;

public interface Printer {

    void init();

    void print(String str);

    void print(Text text);

    void println();

    void println(String str);

    void println(Text text);

    void finish();
}
