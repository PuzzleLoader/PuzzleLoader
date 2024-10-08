package com.github.puzzle.core.loader.util;

import com.github.puzzle.core.loader.meta.ModInfo;
import com.github.puzzle.core.loader.meta.parser.ModJson;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.puzzle.core.loader.meta.parser.VersionParser.hasDependencyVersion;

@SuppressWarnings("UrlHashCode")
public class ModLocator {
    public static final String COSMIC_REACH_SERVER_ENTRYPOINT = "finalforeach.cosmicreach.server.ServerLauncher";
    public static final String COSMIC_REACH_CLIENT_ENTRYPOINT = "finalforeach.cosmicreach.server.ServerLauncher";

    public static Logger LOGGER = LogManager.getLogger("Puzzle | ModLocator");
    public static File MOD_FOLDER = new File("pmods");

    public static Map<String, ModContainer> locatedMods = new HashMap<>();

    @SuppressWarnings("unused")
    public static boolean isModLoaded(String modId) {
        return locatedMods.get(modId) != null;
    }

    public static void setModFolder(File file) {
        MOD_FOLDER = file;
    }

    public static @NotNull Collection<URL> getUrlsOnClasspath() {
        return getUrlsOnClasspath(new ArrayList<>());
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static @NotNull Collection<URL> getUrlsOnClasspath(Collection<URL> urlz) {
        Set<URL> urls = new HashSet<>(urlz);
        // 'urls' may contain URL objects
        // i hope it does

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
                ModJson info = ModJson.fromString(strInfo);
                LOGGER.info("Discovered Dev Mod \"{}\" with ID \"{}\"", info.name(), info.id());
                if(locatedMods.containsKey(info.id()))
                    throw new RuntimeException("mod id \""+info.id()+"\" already used");
                else
                    locatedMods.put(info.id(), new ModContainer(ModInfo.fromModJsonInfo(info), null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void getMods(Collection<URL> classPath) {
        Collection<URL> urls = getUrlsOnClasspath(classPath);

        for (URL url : urls) {
            File file = new File(URLDecoder.decode(url.getFile(), Charset.defaultCharset()));
            if (!file.isDirectory()) {
                try {
                    if (file.exists()) {
                        ZipFile jar = new ZipFile(file, ZipFile.OPEN_READ);
                        ZipEntry modJson = jar.getEntry("puzzle.mod.json");
                        if (modJson != null) {
                            String strInfo = new String(jar.getInputStream(modJson).readAllBytes());
                            ModJson info = ModJson.fromString(strInfo);
                            LOGGER.info("Discovered Mod \"{}\" with ID \"{}\"", info.name(), info.id());
                            if(locatedMods.containsKey(info.id()))
                                throw new RuntimeException("mod id \""+info.id()+"\" already used");
                            else
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
            if (mod.INFO.JSON.dependencies() == null) continue;
            if (mod.INFO.JSON.dependencies().isEmpty()) continue;
            LOGGER.info("Mod deps for {}", mod.ID);
            for (Map.Entry<String, String> entry : mod.INFO.JSON.dependencies().entrySet()) {
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

    public static void crawlModsFolder(Collection<URL> urls) {
        if (!MOD_FOLDER.exists()) {
            if (!MOD_FOLDER.mkdir()) LOGGER.warn("{} could not be created, provide access to java", MOD_FOLDER);
            return;
        }

        for (File modFile : Objects.requireNonNull(MOD_FOLDER.listFiles())) {
            try {
                LOGGER.info("Found Jar/Zip {}", modFile);
                urls.add(modFile.toURI().toURL());
            } catch (Exception ignore) {}
        }
    }
}
