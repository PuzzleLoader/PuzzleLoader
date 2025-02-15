package com.github.puzzle.buildsrx;

import java.net.URL;
import java.net.URLClassLoader;

public class MiniClassLoader extends URLClassLoader {

    public static final MiniClassLoader INSTANCE = new MiniClassLoader(MiniClassLoader.class.getClassLoader());

    public MiniClassLoader(ClassLoader classLoader) {
        super(new URL[0], classLoader);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public Class<?> loadFromBytes(String name, byte[] bytes) {
        return super.defineClass(name, bytes, 0, bytes.length);
    }
}
