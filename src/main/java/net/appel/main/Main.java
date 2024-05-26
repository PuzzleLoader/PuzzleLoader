package net.appel.main;

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

public class Main {
    public static InjectableUrlClassloader AppClassLoader;
    public static Logger logger = LogManager.getLogger("Appel | Main");

    static File CurrentRunningJar = new File(Main.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath());

    public record FileJarPair(
            JarFile jar,
            File file
    ) {}

    public static URL[] getUrls() {
        List<URL> urls = new ArrayList<>();

        try {
//            FileJarPair cosmicReachJar = searchForCosmicReach();
//            if (cosmicReachJar != null) {
//                urls.add(cosmicReachJar.file.toURI().toURL());
//            }
            crawlModsFolder(urls);
            urls.add(CurrentRunningJar.toURI().toURL());
        } catch (Exception ignore) {}

        return urls.toArray(new URL[0]);
    }

    public static void crawlModsFolder(List<URL> urls) {
        File modsFolder = new File("appelMods");
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

    public static String DEFAULT_MAIN_CLASS = "finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher";
    public static String GAME_MAIN_CLASS = DEFAULT_MAIN_CLASS;

    static File lookForJarVariations(String offs) {
        Pattern type1 = Pattern.compile("Cosmic Reach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type2 = Pattern.compile("Cosmic_Reach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type3 = Pattern.compile("CosmicReach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        for (File f : Objects.requireNonNull(new File(offs).listFiles())) {
            if (type1.matcher(f.getName()).find()) return f;
            if (type2.matcher(f.getName()).find()) return f;
            if (type3.matcher(f.getName()).find()) return f;
            if (f.getName().equals("cosmic_reach.jar")) return f;
            if (f.getName().equals("cosmicreach.jar")) return f;
            if (f.getName().equals("cosmicReach.jar")) return f;
        }
        return null;
    }

    static FileJarPair toCrJar(File f) {
        try {
            JarFile crJar = new JarFile(f);
            GAME_MAIN_CLASS = crJar.getManifest().getMainAttributes().getValue("Main-Class");
            return new FileJarPair(crJar, f);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static FileJarPair searchForCosmicReach() {
//        try {
//            ClassLoader.getPlatformClassLoader().loadClass(DEFAULT_MAIN_CLASS);
//        } catch (ClassNotFoundException e) {
//            File jarFile;
//            jarFile = lookForJarVariations(".");
//            if (jarFile != null) return toCrJar(jarFile);
//            jarFile = lookForJarVariations("../");
//            if (jarFile != null) return toCrJar(jarFile);
//        }
        return null;
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
