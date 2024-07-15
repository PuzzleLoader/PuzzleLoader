package dev.crmodders.puzzle.core.loader.mod;

import com.google.gson.Gson;
//import dev.crmodders.puzzle.core.game.internal.mods.FluxPuzzle;
import dev.crmodders.puzzle.core.game.internal.mods.FluxPuzzle;
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

    public static void AddBuiltinMods(IGameProvider provider) {

        /* Puzzle Loader as a Mod */
        ModInfo.Builder puzzleLoaderInfo = ModInfo.Builder.New();
        {
            puzzleLoaderInfo.setName("Puzzle Loader");
            puzzleLoaderInfo.setDesc("A new dedicated modloader for Cosmic Reach");
            puzzleLoaderInfo.addEntrypoint("transformers", PuzzleTransformers.class.getName());
            puzzleLoaderInfo.addDependency("cosmic-reach", provider.getGameVersion());
            puzzleLoaderInfo.addMixinConfigs(
                    "internal.mixins.json",
                    "accessors.mixins.json",
                    "bugfixes.mixins.json"
            );
            puzzleLoaderInfo.setAccessTransformerType(
                    AccessTransformerType.ACCESS_MANIPULATOR,
                    "puzzle_loader.manipulator"
            );

            ModLocator.LocatedMods.put("puzzle-loader", puzzleLoaderInfo.build().getOrCreateModContainer());
        }

        /* Cosmic Reach as a mod */
        ModInfo.Builder cosmicReachInfo = ModInfo.Builder.New();
        {
            cosmicReachInfo.setName(provider.getName());
            cosmicReachInfo.setDesc("The base Game");
            cosmicReachInfo.addAuthor("FinalForEach");
            ModLocator.LocatedMods.put(provider.getId(), cosmicReachInfo.build().getOrCreateModContainer());
        }

        /* Flux API as a mod */
        ModInfo.Builder fluxAPIInfo = ModInfo.Builder.New();
        {
            fluxAPIInfo.setName("Flux API");
            fluxAPIInfo.setId("fluxapi");
            fluxAPIInfo.setVersion("0.7.4");
            fluxAPIInfo.setDesc("The central modding API for Cosmic Reach Fabric/Quilt/Puzzle");
            fluxAPIInfo.addEntrypoint("preInit", FluxPuzzle.class.getName());
            fluxAPIInfo.addDependency("cosmic-reach", provider.getGameVersion());
            fluxAPIInfo.addDependency("puzzle-loader", Version.parseVersion("1.0.0"));
            fluxAPIInfo.addAuthors("Mr Zombii", "Nanobass", "CoolGI");
            fluxAPIInfo.addMixinConfig("fluxapi.mixins.json");

            ModLocator.LocatedMods.put("fluxapi", fluxAPIInfo.build().getOrCreateModContainer());
        }
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
