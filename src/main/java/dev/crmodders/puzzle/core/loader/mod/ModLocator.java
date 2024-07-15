package dev.crmodders.puzzle.core.loader.mod;

import com.google.gson.Gson;
//import dev.crmodders.puzzle.core.game.internal.mods.FluxPuzzle;
//import dev.crmodders.puzzle.core.game.internal.mods.FluxPuzzle;
import dev.crmodders.puzzle.core.loader.launch.internal.mods.PuzzleTransformers;
import dev.crmodders.puzzle.core.loader.mod.info.ModInfo;
import dev.crmodders.puzzle.core.loader.providers.api.IGameProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModLocator {

    public static Logger logger = LogManager.getLogger("Puzzle | ModLocator");
    public static Gson gsonInstance = new Gson();

    public static Map<String, ModContainer> LocatedMods = new HashMap<>();

    public static Collection<URL> getUrlsOnClasspath() {
        return getUrlsOnClasspath(new ArrayList<>());
    }

    public static Collection<URL> getUrlsOnClasspath(Collection<URL> urlz) {
        Set<URL> urls = new HashSet<>(urlz);

        if (ModLocator.class.getClassLoader() instanceof URLClassLoader loader) {
            Collections.addAll(urls, loader.getURLs());
        } else {
            for (String url : System.getProperty("java.class.path").split(File.pathSeparator)) {
                try {
                    urls.add(new File(url).toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        return urls;
    }

    public static void getMods() {
        getMods(new ArrayList<>());
    }

    public static void getMods(Collection<URL> classPath) {
        Collection<URL> urls = getUrlsOnClasspath(classPath);

        for (URL url : urls) {
            File file = new File(url.getFile());

            if (!file.isDirectory()) {
                try {
                    if (file.exists()) {
                        ZipFile jar = new ZipFile(file, ZipFile.OPEN_READ);
                        ZipEntry modJson = jar.getEntry("puzzle.mod.json");
                        if (modJson != null) {
                            String strInfo = new String(jar.getInputStream(modJson).readAllBytes());
                            ModJsonInfo info = gsonInstance.fromJson(strInfo, ModJsonInfo.class);
                            logger.info("Discovered Mod \"{}\" with ID \"{}\"", info.name(), info.id());
                            LocatedMods.put(info.id(), new ModContainer(ModInfo.fromModJsonInfo(info), jar));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    // TODO: Verify Mod Dependencies (Partial completed by repletsin5)
    public static void verifyDependencies() {
        logger.warn("Can't check mod version. No support for semantic versioning");
        for(var mod : LocatedMods.values()){
            logger.info("Mod deps for {}", mod.ID);
            for (Map.Entry<String, Version> entry : mod.INFO.RequiredDependencies.entrySet()) {
                logger.info("\t{}: {}", entry.getKey(),entry.getValue());
                if(LocatedMods.get(entry.getKey()) == null){
                    logger.fatal("can not find mod dependency: {} for mod id: {}", entry.getKey(),mod.ID);
                }
                else {
                    //TODO: Version checking. Need semantic versioning compat and the ability to define allowed versions.
                    return;
                }
            }
        }
    }

    public static void crawlModsFolder(Collection<URL> urls) {
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

    public static <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) {
        ModLocator.LocatedMods.values().forEach(container -> {
            container.invokeEntrypoint(key, type, invoker);
        });
    }

}
