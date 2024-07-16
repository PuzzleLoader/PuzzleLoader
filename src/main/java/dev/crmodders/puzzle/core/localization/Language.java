package dev.crmodders.puzzle.core.localization;

import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.core.game.PuzzleRegistries;
import dev.crmodders.puzzle.loader.launch.Piece;
import dev.crmodders.puzzle.core.localization.parsers.LangCrypticParser;
import dev.crmodders.puzzle.core.localization.parsers.LangJsonParser;
import dev.crmodders.puzzle.core.localization.parsers.LangParser;
import dev.crmodders.puzzle.loader.mod.Version;
import dev.crmodders.puzzle.core.registries.IRegistry;
import dev.crmodders.puzzle.core.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static dev.crmodders.puzzle.loader.mod.ModLocator.getUrlsOnClasspath;

public class Language {

    public static Language defaultGameLang;
    public static Language currentLanguage;
    // maybe register?
    public static HashMap<String, LangParser> parsers = new HashMap<>();
    public static final Logger logger = LogManager.getLogger("Puzzle | Language");

    static {
        parsers.put("json", new LangJsonParser());
        parsers.put("cr", new LangCrypticParser(false));
    }


    public String languageTag;
    public Version version;
    public String[] namespaces;
    public HashMap<String, String> translations = new HashMap<>();


    public String translate(String textId) {
        String id = textId.replace("::", ".");
        // more handling later?

        // support a standardized textId? world.exit.message? world.exitMessage? world::exit::message? world::exitMessage?
        // or parse both to dot form, kinda did this alerady lol

        if (translations.containsKey(id)) {
            return translations.get(id);
        } else if (defaultGameLang.translations.containsKey(id)) {
            return defaultGameLang.translations.get(id);
        }

        return id;
    }



    // move all static to here

    // should this be File?
    public static Identifier registerLanguage(String languageFile) {
        Collection<URL> urls = getUrlsOnClasspath(List.of(Piece.classLoader.getURLs()));
        for (URL url : urls) {
            File file = new File(url.getFile());
            if (!file.isDirectory()) {
                Identifier id = registerLanguage(file, languageFile);
                if (id != null) return id;
            }
        }
        return null;
    }

    public static Identifier registerLanguage(File classpath, String languageFile) {
        try {
            if (classpath.exists()) {
                String source;
                try {
                    ZipFile jar = new ZipFile(classpath, ZipFile.OPEN_READ);
                    ZipEntry lang = jar.getEntry(languageFile);
                    source = new String(jar.getInputStream(lang).readAllBytes());
                } catch (Exception e) {
                    logger.warn("falling back to default classpath");
                    source = new String(Language.class.getClassLoader().getResourceAsStream(languageFile).readAllBytes());
                }

                // can def be optimized
                String temp = languageFile.replaceAll("\\\\", "/");
                temp="/"+temp;
                String name = temp.substring(temp.lastIndexOf("/"));
                String[] parts = name.split("\\.");

                Language language = null;

                if (parsers.containsKey(parts[parts.length-1])) {
                    language = parsers.get(parts[parts.length-1]).parse(source);
                } else {
                    // should it really fall back to checking the source of the file?
                    for (LangParser parser : parsers.values()) {
                        System.out.println(parser.canParse(name, source));
                        if (parser.canParse(name, source)) {
                            language = parser.parse(source); // placeholder
                            if (language != null) break;
                        }
                    }
                }

                if (language != null) {
                    // Indentifier(namespace, languagetag)?
                    Language finalLanguage = language;
                    Identifier id = new Identifier("language", language.languageTag);
                    IRegistry.register(PuzzleRegistries.PuzzleLanguages, id, () -> finalLanguage);
                    System.out.println("Identifier is "+id);
                    return id;
                } else {
                    logger.error("Cannot locate a language file parser for "+languageFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cannot open classpath "+classpath);
        }

        return null;
    }

    public static String translate(Identifier id, String textId) {
        return getLanguage(id).translate(textId);
    }

    public static Language getLanguage(Identifier id) {
        Language language;
        try {
            language = new RegistryObject<>(PuzzleRegistries.PuzzleLanguages, id).getObject();
        } catch (Exception e) {
            language = defaultGameLang; // ? default or current?
        }
        return language;
    }
}