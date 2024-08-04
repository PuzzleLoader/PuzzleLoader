package com.github.puzzle.loader.mod;

import com.github.puzzle.loader.mod.info.ModInfo;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.puzzle.loader.mod.VersionParser.hasDependencyVersion;

public class ModLocator {
    public static Logger LOGGER = LogManager.getLogger("Puzzle | ModLocator");
    public static Gson gsonInstance = new Gson();

    public static Map<String, ModContainer> locatedMods = new HashMap<>();

    public static @NotNull Collection<URL> getUrlsOnClasspath() {
        return getUrlsOnClasspath(new ArrayList<>());
    }

    public static @NotNull Collection<URL> getUrlsOnClasspath(Collection<URL> urlz) {
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

    public static void walkDir(File file) {
        if (file.isDirectory()) {
            if (file.listFiles() != null) {
                Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(ModLocator::walkDir);
            }
        } else if (file.getName().equals("puzzle.mod.json")) {
            try {
                String strInfo = new String(new FileInputStream(file).readAllBytes());
                ModJsonInfo info = ModJsonInfo.fromString(strInfo);
                LOGGER.info("Discovered Dev Mod \"{}\" with ID \"{}\"", info.name(), info.id());
                locatedMods.put(info.id(), new ModContainer(ModInfo.fromModJsonInfo(info), null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
                            ModJsonInfo info = ModJsonInfo.fromString(strInfo);
                            LOGGER.info("Discovered Mod \"{}\" with ID \"{}\"", info.name(), info.id());
                            locatedMods.put(info.id(), new ModContainer(ModInfo.fromModJsonInfo(info), jar));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                walkDir(file);
            }
        }

    }

    public static void verifyDependencies() {
        LOGGER.warn("Warning! Only partial semantic versioning support");
        for(var mod : locatedMods.values()){
            if (mod.INFO.JsonInfo.dependencies() != null)
                if(!mod.INFO.JsonInfo.dependencies().isEmpty()) {
                    LOGGER.info("Mod deps for {}", mod.ID);
                    for (Map.Entry<String, String> entry : mod.INFO.JsonInfo.dependencies().entrySet()) {
                        LOGGER.info("\t{}: {}", entry.getKey(), entry.getValue());
                        var modDep = locatedMods.get(entry.getKey());
                        if (modDep == null) {
                            throw new RuntimeException(String.format("can not find mod dependency: %s for mod id: %s", entry.getKey(), mod.ID));
                        } else {
                            if (!hasDependencyVersion(modDep.VERSION, entry.getValue())) {
                                throw new RuntimeException(String.format("Mod id: %s, requires: %s version of %s, got: %s", mod.ID, entry.getValue(), modDep.ID, modDep.VERSION));
                            }
                        }
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
                LOGGER.info("Found Jar/Zip {}", modFile);
                urls.add(modFile.toURI().toURL());
            } catch (Exception ignore) {}
        }
    }

    public static <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) {
        ModLocator.locatedMods.values().forEach(container -> {
            try {
                container.invokeEntrypoint(key, type, invoker);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
