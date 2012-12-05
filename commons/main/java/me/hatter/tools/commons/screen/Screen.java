package me.hatter.tools.commons.screen;

import java.util.ArrayList;
import java.util.List;

import jline.Terminal;
import jline.TerminalFactory;
import me.hatter.tools.commons.color.Font;

// TODO
public class Screen {

    private Terminal     terminal;
    volatile private int width;
    volatile private int height;
    private List<Line>   outputs = new ArrayList<Line>();

    public Screen() {
        terminal = TerminalFactory.create();
        Thread terminalMonitor = new Thread() {

            public void run() {
                while (true) {
                    int w = terminal.getWidth();
                    int h = terminal.getHeight();
                    width = w;
                    height = h;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        terminalMonitor.setName("Terminal width/height monitor");
        terminalMonitor.setDaemon(true);
        terminalMonitor.start();
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void reset() {
        int w = width;
        int h = height;
        for (int i = 0; i < outputs.size(); i++) {
            Line l = outputs.get(i);
        }
        if (h != outputs.size()) {
            if (h < outputs.size()) {

            }
            if (h > outputs.size()) {

            }
        }
    }

    public void println(Font font, String text) {

    }

    public void display() {

    }
}
