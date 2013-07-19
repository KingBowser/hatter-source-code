package me.hatter.tools.stormloader;

import java.net.URL;
import java.net.URLClassLoader;

public class StormClassLoader extends URLClassLoader {

    public static void main(String[] args) {
        ClassLoader ext = ClassLoader.getSystemClassLoader().getParent();
        System.out.println(ext);
    }

    public StormClassLoader(URL[] urls) {
        super(urls);
    }

    public StormClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

}
