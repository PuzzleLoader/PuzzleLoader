package net.appel.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class InjectableUrlClassloader extends URLClassLoader {
    public InjectableUrlClassloader(URL[] urls) {
        super(urls);
    }

    public void addUrl(URL url) {
        super.addURL(url);
    }


}
