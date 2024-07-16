package dev.crmodders.puzzle.core.localization;


import dev.crmodders.puzzle.core.localization.parsers.LangCrypticParser;
import dev.crmodders.puzzle.core.localization.parsers.LangJsonParser;
import dev.crmodders.puzzle.core.localization.parsers.LangParser;
import dev.crmodders.puzzle.core.localization.parsers.ParseResult;
import dev.crmodders.puzzle.core.registries.IRegistry;
import dev.crmodders.puzzle.core.registries.RegistryObject;
import dev.crmodders.puzzle.core.resources.Identifier;
import dev.crmodders.puzzle.game.PuzzleRegistries;
import dev.crmodders.puzzle.loader.launch.Piece;
import dev.crmodders.puzzle.loader.mod.Version;
import dev.crmodders.puzzle.utils.AnsiColours;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static dev.crmodders.puzzle.loader.mod.ModLocator.getUrlsOnClasspath;

public class Language {

    public static Language defaultGameLang;
    public static Language currentLanguage;
    public static Map<String, LangParser> parsers = new HashMap<>();
    private static final List<String> cannotParse = new ArrayList<>();
    public static final Logger logger = LogManager.getLogger("Puzzle | Language");

    static {
        parsers.put("json", new LangJsonParser());
        parsers.put("cr", new LangCrypticParser(true));
    }

    public String languageTag;
    public Version version;
    public String[] namespaces;
    public Map<String, String> translations = new HashMap<>();

    public String translate(String textId) {
        String id = textId.replace("::", ".");
        return translations.getOrDefault(id, defaultGameLang.translations.getOrDefault(id, id));
    }

    public static Identifier registerLanguage(String languageFile) {
        Collection<URL> urls = getUrlsOnClasspath(List.of(Piece.classLoader.getURLs()));
        logger.warn("Attempting to locate a language file parser for \"{}\"", AnsiColours.apply(languageFile, AnsiColours.YELLOW));
        for (URL url : urls) {
            File file = new File(url.getFile());
            if (!file.isDirectory()) {
                Identifier id = registerLanguage(file, languageFile);
                if (id != null) return id;
                if (cannotParse.contains(languageFile)) break;
            }
        }
        logger.error("Cannot locate a language file parser for \"{}\"", AnsiColours.apply(languageFile, AnsiColours.YELLOW));
        return null;
    }

    private static Identifier registerLanguage(File classpath, String languageFile) {
        if (!classpath.exists()) {
            logger.error("Classpath \"{}\" does not exist", AnsiColours.apply(classpath, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode());
            return null;
        }

        String source = getLanguageFileSource(classpath, languageFile);
        if (source == null) return null;

        Language language = parseLanguage(source, languageFile);

        if (language != null) {
            Identifier id = new Identifier("language", language.languageTag);
            IRegistry.register(PuzzleRegistries.PuzzleLanguages, id, () -> language);
            logger.info("Registered language {}", id);
            return id;
        } else {
            return null;
        }
    }

    private static String getLanguageFileSource(File classpath, String languageFile) {
        try (ZipFile jar = new ZipFile(classpath, ZipFile.OPEN_READ)) {
            ZipEntry lang = jar.getEntry(languageFile);
            if (lang != null) return new String(jar.getInputStream(lang).readAllBytes());
            else logger.warn("Language file \"{}\" not found in classpath \"{}\"", AnsiColours.apply(languageFile, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode(), AnsiColours.apply(classpath, AnsiColours.YELLOW));
        } catch (IOException e) {
            logger.warn("Falling back to default classpath for file \"{}\":\"{}\"", AnsiColours.apply(languageFile, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode(), AnsiColours.apply(e.getMessage(), AnsiColours.RED));
        }
        try (InputStream stream = Language.class.getClassLoader().getResourceAsStream(languageFile)) {
            if (stream != null) return new String(stream.readAllBytes());
            else logger.error("Language file \"{}\" not found in default classpath", AnsiColours.apply(languageFile, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode());
        } catch (IOException e) {
            logger.error("Error reading language file \"{}\" from default classpath \"{}\"", AnsiColours.apply(languageFile, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode(), AnsiColours.apply(e.getMessage(), AnsiColours.RED));
        }
        return null;
    }

    private static Language parseLanguage(String source, String languageFile) {
        String name = getName(languageFile);
        String extension = getExtension(languageFile);

        ParseResult result = null;
        if (parsers.containsKey(extension)) {
            result = parsers.get(extension).parse(source);
        }

        if (result == null || (result.canParse && result.isError())) {
            for (LangParser parser : parsers.values()) {
                result = parser.canParse(name, source);

                if (result.canParse && !result.isError()) {
                    result = parser.parse(source);
                    if (result.canParse && !result.isError()) break;
                    else if (result.isError()) {
                        logger.error("Error parsing Language File \"{}\": \"{}\"", AnsiColours.apply(languageFile, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode(), AnsiColours.apply(result.getErrorValue(), AnsiColours.RED));
                        cannotParse.add(languageFile);
                        break;
                    }
                } else {
                    System.out.println("help me");
                    logger.error("Error parsing Language File \"{}\": \"{}\"", AnsiColours.apply(languageFile, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode(), AnsiColours.apply(result.getErrorValue(), AnsiColours.RED));
                    cannotParse.add(languageFile);
                    break;
                }
            }
        }
        return result != null && !result.isError() ? result.getParsedValue() : null;
    }

    public static String translate(Identifier id, String textId) {
        return getLanguage(id).translate(textId);
    }

    public static Language getLanguage(Identifier id) {
        Language language;
        try {
            language = new RegistryObject<>(PuzzleRegistries.PuzzleLanguages, id).getObject();
        } catch (Exception e) {
            logger.error("Error getting language with identifier \"{}\":\"{}\"", AnsiColours.apply(id, AnsiColours.YELLOW)+AnsiColours.WHITE.getCode(), AnsiColours.apply(e.getMessage(), AnsiColours.RED));
            language = defaultGameLang; // Default to current or default game language?
        }
        return language;
    }

    private static String getName(String file) {
        String name = file.replace("\\", "/");
        int index = name.lastIndexOf("/");
        return index == -1 ? name : name.substring(index + 1);
    }

    private static String getExtension(String file) {
        String[] parts = getName(file).split("\\.");
        return parts[parts.length - 1];
    }
}
