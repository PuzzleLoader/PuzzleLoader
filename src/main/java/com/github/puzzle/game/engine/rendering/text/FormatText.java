package com.github.puzzle.game.engine.rendering.text;

import com.badlogic.gdx.graphics.Color;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FormatText {
    public static final String FORMAT_KEY = "§";

    public static final Pattern FORMAT_PATTER = Pattern.compile("(?i)"+FORMAT_KEY+"[0-9A-FR]");

    public static FormatText of(String text){
        return new FormatText(text).parse();
    }
    public FormatText(String text){
        this.text = text;
    }

    public Color toColor(String c){
        return switch (c) {
            case "0", "r" -> Color.WHITE.cpy();
            case "1" -> Color.BLACK.cpy();
            case "2" -> Color.BLUE.cpy();
            case "3" -> Color.RED.cpy();
            case "4" -> Color.YELLOW.cpy();
            case "5" -> Color.GREEN.cpy();
            case "6" -> Color.PINK.cpy();
            default -> null;
        };
    }

    public FormatText parse() {
        String fixedText = new String(text.getBytes(StandardCharsets.UTF_8));
        if (!hasParsed) {
            if (fixedText.contains(FORMAT_KEY)) {
                var matcher = FORMAT_PATTER.matcher(fixedText);
                var split = FORMAT_PATTER.split(fixedText);
                if (split.length > 1) {
                    while (matcher.find()) {
                        var s = matcher.group(0);
                        parseIndex.put(split[parseIndex.size()+1], toColor( s.replaceAll(FORMAT_KEY,"")));
                    }

                }
                if(!split[0].isBlank() || !split[0].isEmpty())
                    parseIndex.put(split[0],null);
            }
            hasParsed = true;
        }
        return this;
    }
    public String strip(){
        return text == null ? null : FORMAT_PATTER.matcher(text).replaceAll("");
    }

    private final String text;
    private  boolean hasParsed = false;
    public final Map<String, Color> parseIndex = new HashMap<>();


    public String toString(){
        return text;
    }

}
