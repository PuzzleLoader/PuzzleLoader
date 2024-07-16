package dev.crmodders.puzzle.core.localization;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationString {

    private static final Pattern pattern = Pattern.compile("\\{([^}]*)\\}");

    private final String translation;

    private String compiled;

    private int[] compiledOffsets;

    public TranslationString(String translation) {
        this.translation=translation;
        this.compiled = translation;
        this.compiledOffsets = new int[0];
    }

    public void compile(List<String> parameters) {
        Matcher matcher = pattern.matcher(translation);

        StringBuilder compiled = new StringBuilder();
        compiledOffsets = new int[parameters.size()];
        int argument = 0;

        while(matcher.find()) {
            String param = matcher.group(1);
            int index = parameters.indexOf(param);
            compiledOffsets[argument] = index;
            matcher.appendReplacement(compiled, "%s");
            argument++;
        }
        matcher.appendTail(compiled);

        this.compiled = compiled.toString();
    }

    public String format(Object... arguments){
        if(compiledOffsets.length == 0) {
            return compiled;
        }
        Object[] args = new Object[arguments.length];
        for(int i = 0; i < args.length; i++) {
            args[i] = arguments[compiledOffsets[i]];
        }
        return compiled.formatted(args);
    }

    public String string() { return translation; }

    @Override
    public String toString() {
        return translation + "[" + compiled + "]";
    }
}
