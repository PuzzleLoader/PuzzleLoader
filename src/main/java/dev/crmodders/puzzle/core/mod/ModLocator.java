package dev.crmodders.puzzle.core.mod;

import com.google.gson.Gson;
//import dev.crmodders.puzzle.core.internalMods.FluxPuzzle;
import dev.crmodders.puzzle.core.internalMods.PuzzleTransformers;
import dev.crmodders.puzzle.core.providers.api.IGameProvider;
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

    public static void AddBuiltinMods(IGameProvider provider) {
        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("cosmic-reach", provider.getGameVersion().toString());

        Map<String, Collection<String>> entrypoints = new HashMap<>();
        entrypoints.put("transformers", List.of(PuzzleTransformers.class.getName()));

        /* Puzzle Loader as a Mod */
        ModLocator.LocatedMods.put("puzzle-loader", new ModContainer(new ModJsonInfo(
                "puzzle-loader",
                "1.0.0",
                "Puzzle Loader",
                "A new dedicated modloader for Cosmic Reach",
                new String[] { "Zombii" },
                entrypoints,
                new HashMap<>(),
                new String[]{ "internal.mixins.json", "accessors.mixins.json", "bugfixes.mixins.json" },
                dependencies,
//                "puzzle_loader.manipulator",
                null,
                "META-INF/accesstransformer.cfg",
                null
//                "puzzle_loader.accesswidener"
        )));

        /* Cosmic Reach as a mod */
        ModLocator.LocatedMods.put(provider.getId(), new ModContainer(new ModJsonInfo(
                provider.getId(),
                provider.getGameVersion().toString(),
                provider.getName(),
                "The base Game",
                new String[] { "FinalForEach" },
                new HashMap<>(),
                new HashMap<>(),
                new String[]{},
                new HashMap<>(),
                null,
                null,
                null
        )));

        Map<String, String> fluxDependencies = new HashMap<>();
        fluxDependencies.put("cosmic-reach", provider.getGameVersion().toString());
        fluxDependencies.put("puzzle-loader", "1.0.0");

//        Map<String, Collection<String>> fluxEntrypoints = new HashMap<>();
//        fluxEntrypoints.put("preInit", List.of(FluxPuzzle.class.getName()));
//
//        /* Flux API as a mod */
//        ModLocator.LocatedMods.put("fluxapi", new ModContainer(new ModJsonInfo(
//                "fluxapi",
//                "0.7.3",
//                "Flux API",
//                "The central modding API for Cosmic Reach Fabric/Quilt/Puzzle",
//                new String[] { "Mr Zombii", "Nanobass", "CoolGI" },
//                fluxEntrypoints,
//                new HashMap<>(),
//                new String[]{ "fluxMicro.mixins.json" },
//                fluxDependencies,
//                null
//        )));
    }

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
                            LocatedMods.put(info.id(), new ModContainer(info, jar));
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
            for (Map.Entry<String, String> entry : mod.JSON_INFO.dependencies().entrySet()) {
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
