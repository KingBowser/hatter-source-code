package me.hatter.tests.jlinetest;

import java.io.IOException;

import jline.Terminal;
import jline.TerminalFactory;

public class JLineTest {

    public static void main(String[] args) throws IOException {
        // System.out.print("Input password: ");
        // String p = (new jline.console.ConsoleReader()).readLine(new Character('*'));
        // System.out.println("P: " + p);
        Terminal t = TerminalFactory.create();
        System.out.println(t.getWidth());
        System.out.println(t.getHeight());
        
    }
}
