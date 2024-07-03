package dev.crmodders.puzzle.access_manipulator;

import dev.crmodders.puzzle.access_manipulator.pairs.FieldModifierPair;
import dev.crmodders.puzzle.access_manipulator.pairs.MethodModifierPair;
import dev.crmodders.puzzle.access_manipulator.transformers.AccessManipulatorTransformer;
import dev.crmodders.puzzle.access_manipulator.transformers.ClassModifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AccessTransformerReader {

    public static void read(String contents) {
        try {
            readTransformer(contents.replaceAll("\\(", " ("));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void readTransformer(String contents) throws IOException {
        BufferedReader reader =  new BufferedReader(new StringReader(contents));
        String ln;
        while((ln = reader.readLine())!=null){
            if(ln.isBlank() || ln.isEmpty() || ln.startsWith("#"))
                continue;
            List<String> tokens = Arrays.asList(Pattern.compile("[ \\t]+").split(ln));
            ClassModifier modifier;
            var access = tokens.get(0);
            modifier = switch (access) {
                case "public" -> ClassModifier.PUBLIC;
                case "private" -> ClassModifier.PRIVATE;
                case "protected" -> ClassModifier.PROTECTED;

                case "public+f" -> ClassModifier.PUBLIC_IMMUTABLE;
                case "private+f" -> ClassModifier.PRIVATE_IMMUTABLE;
                case "protected+f" -> ClassModifier.PROTECTED_IMMUTABLE;

                case "public-f" -> ClassModifier.PUBLIC_MUTABLE;
                case "private-f" -> ClassModifier.PRIVATE_MUTABLE;
                case "protected-f" -> ClassModifier.PROTECTED_MUTABLE;
                default -> throw new RuntimeException("Unsupported access: '" + tokens.get(0) + "'");
            };
            switch (tokens.size()) {
                case 2:
                    AccessManipulatorTransformer.classesToModify.put(tokens.get(1).replaceAll("\\.", "/"),modifier);
                    break;
                case 3:
                    HashMap<String, FieldModifierPair> hm = new HashMap<>();
                    hm.put(tokens.get(2),new FieldModifierPair(tokens.get(2), tokens.get(1).replaceAll("\\.", "/"), modifier));
                    AccessManipulatorTransformer.fieldsToModify.put(tokens.get(1).replaceAll("\\.", "/"), hm);
                    break;
                case 4:
                    List<MethodModifierPair> p = new ArrayList<>(1);
                    p.add(new MethodModifierPair(tokens.get(2),tokens.get(3),tokens.get(1).replaceAll("\\.", "/"), modifier));
                    AccessManipulatorTransformer.methodsToModify.put(tokens.get(1).replaceAll("\\.", "/"),p);
                    break;
                default:
                    throw new RuntimeException("Unsupported type: '" + tokens.get(1) + "'");
            }
        }
    }

}