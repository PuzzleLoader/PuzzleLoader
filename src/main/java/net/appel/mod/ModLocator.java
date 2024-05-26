package net.appel.mod;

import com.google.gson.Gson;
import net.appel.mod.info.ModJsonInfo;
import net.appel.mod.interfaces.ModInitializer;
import net.appel.mod.interfaces.ModPreInitializer;
import net.appel.registry.BuiltInRegistries;
import net.appel.tags.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.slf4j.helpers.MessageFormatter;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModLocator {

    public static Logger logger = LogManager.getLogger("ModLocator");

    public static Gson gson = new Gson();

    static List<String> getJarsOnClasspath() {
        String classpath = System.getProperty("java.class.path");
        String[] classPathValues = classpath.split(File.pathSeparator);
        return new ArrayList<>(Arrays.asList(classPathValues));
    }

    static record IdSetPair(
            Identifier id,
            Set<String> prefixes
    ) {}

    public static void locateModsOnClasspath() {
        getJarsOnClasspath().forEach((jarPath) -> {
            File jar = new File(jarPath);
            if (!jar.isDirectory() && jar.exists()) {
                IdSetPair pair = getAllPrefixesInFile(jar);
                if (!pair.prefixes.isEmpty())
                    pair.prefixes.forEach((prefix) -> {
                        getPackagePrefixes(pair.id, prefix);
                    });
            }
        });
    }

    public static void locateModsOnClasspath(List<URL> urls) {
        urls.forEach((jarPath) -> {
            File jar = new File(jarPath.getFile().replaceFirst("\\\\", "").replaceAll("%20", " "));
            if (!jar.isDirectory() && jar.exists()) {
                IdSetPair pair = getAllPrefixesInFile(jar);
                if (!pair.prefixes.isEmpty())
                    pair.prefixes.forEach((prefix) -> {
                        getPackagePrefixes(pair.id, prefix);
                    });
            }
        });
    }

    static IdSetPair getAllPrefixesInFile(File givenFile) {
        Set<String> prefixes = new HashSet<>();
        try {
            if (givenFile.isDirectory()) return new IdSetPair(null, new HashSet<>());
            JarFile jarFile = new JarFile(givenFile);
            JarEntry entry = jarFile.getJarEntry("appel.mod.json");
            if (entry == null) {
                return new IdSetPair(null, new HashSet<>());
            }
            logger.info("Found Mod Called: {}", jarFile.getName());
            byte[] jsonBytes = jarFile.getInputStream(entry).readAllBytes();
            ModJsonInfo modJsonInfo = gson.fromJson(new String(jsonBytes, StandardCharsets.UTF_8), ModJsonInfo.class);
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    prefixes.add(className.split("\\.")[0]);
                }
            }
            BuiltInRegistries.MOD_INFO_REGISTRY.register(Identifier.fromString(modJsonInfo.id()), modJsonInfo);
            return new IdSetPair(Identifier.fromString(modJsonInfo.id()), prefixes);
        } catch (Exception e) {
            logger.error("Could Not Read Possible Jar File", e);
            return new IdSetPair(null, new HashSet<>());
        }
    }

    public static void getPackagePrefixes(Identifier modId, String prefix) {
        Reflections reflections = new Reflections(prefix);

        reflections.getSubTypesOf(ModPreInitializer.class).forEach((mod) -> {
            Identifier modEntryId = Identifier.fromString(modId.name + "_" + mod.getSimpleName());
            logger.info("Found PRE_INIT mod of fileName {}", mod.getName());
            BuiltInRegistries.PRE_MOD_REGISTRY.register(modEntryId, mod);
        });

        reflections.getSubTypesOf(ModInitializer.class).forEach((mod) -> {
            Identifier modEntryId = Identifier.fromString(modId.name + "_" + mod.getSimpleName());
            logger.info("Found INIT mod of fileName {}", mod.getName());
            BuiltInRegistries.MOD_REGISTRY.register(modEntryId, mod);
            logger.info(BuiltInRegistries.MOD_REGISTRY.getRegisteredNames().length);
        });
    }

    public static void loadPreInitMods() {
        logger.info("Found {} PreInit Mods", BuiltInRegistries.PRE_MOD_REGISTRY.getRegisteredNames().length);
        Arrays.stream(BuiltInRegistries.PRE_MOD_REGISTRY.getRegisteredNames()).forEach((classId) -> {
            Class<? extends ModPreInitializer> clazz = BuiltInRegistries.PRE_MOD_REGISTRY.get(classId);
            try {
                ModPreInitializer preInitializer = clazz.newInstance();
                logger.info("Loading Mod {}", classId);
                preInitializer.onPreInit();
            } catch (Exception e) {
                logger.error(MessageFormatter.basicArrayFormat("Could Not Load Mod Entrypoint {} during phase PRE_INIT", new Object[]{
                        classId
                }), e);
            }
        });
    }
    public static void loadInit() {
        logger.info("Found {} Basic Mods", BuiltInRegistries.MOD_REGISTRY.getRegisteredNames().length);
        Arrays.stream(BuiltInRegistries.MOD_REGISTRY.getRegisteredNames()).forEach((classId) -> {
            Class<? extends ModInitializer> clazz = BuiltInRegistries.MOD_REGISTRY.get(classId);
            logger.info("Loading Mod {}", classId);
            try {
                ModInitializer initializer = clazz.newInstance();
                initializer.onInit();
            } catch (Exception e) {
                logger.error(MessageFormatter.basicArrayFormat("Could Not Load Mod Entrypoint {} during phase INIT", new Object[]{
                        classId
                }), e);
            }
        });
    }

}
