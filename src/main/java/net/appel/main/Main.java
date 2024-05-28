package net.appel.main;

import dev.crmodders.puzzle.launch.Piece;
import net.appel.classloader.InjectableUrlClassloader;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import static dev.crmodders.puzzle.launch.Piece.searchForCosmicReach;

public class Main {
    public static InjectableUrlClassloader AppClassLoader;
    public static Logger logger = LogManager.getLogger("Puzzle | Main");

    static File CurrentRunningJar = new File(Main.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath());


    public static URL[] getUrls() {
        List<URL> urls = new ArrayList<>();

        try {
            Piece.FileJarPair cosmicReachJar = searchForCosmicReach();
            if (cosmicReachJar != null) {
                urls.add(cosmicReachJar.file().toURI().toURL());
            }
            crawlModsFolder(urls);
            urls.add(CurrentRunningJar.toURI().toURL());
        } catch (Exception ignore) {}

        return urls.toArray(new URL[0]);
    }

    public static void crawlModsFolder(List<URL> urls) {
        File modsFolder = new File("pmods");
        if (!modsFolder.exists()) {
            modsFolder.mkdir();
            return;
        }

        for (File modFile : Objects.requireNonNull(modsFolder.listFiles())) {
            try {
                logger.info("Found Jar {}", modFile);
                urls.add(modFile.toURI().toURL());
            } catch (Exception ignore) {}
        }
    }

    

    public static void searchForMods(InjectableUrlClassloader classloader) {
        File modsFolder = new File("appelMods");
        if (!modsFolder.exists()) {
            try {
                modsFolder.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        for (File file : Objects.requireNonNull(modsFolder.listFiles())) {
            try {
                classloader.addUrl(file.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AppClassLoader = new InjectableUrlClassloader(getUrls());
//        searchForMods(AppClassLoader);
        AppClassLoader.loadClass(Launch.class.getName())
                .getMethod("main", String[].class)
                .invoke(null, (Object) args);
    }

}
