package com.github.puzzle.game.ui.credits;

import com.github.puzzle.game.ui.credits.categories.CreditCategory;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class CreditFile {

    final Collection<CreditCategory> categories;

    CreditFile(final Collection<CreditCategory> categories) {
        this.categories = categories;
    }

    final static Pattern VANILLA_CATEGORY = Pattern.compile("(?i)==.+==");

    public static CreditFile fromVanilla(String txt) {
        List<CreditCategory> categories = new ArrayList<>();

        int categoryIndex = -1;
        for (String line : txt.split("\n")) {
            line = line.strip();

            if (VANILLA_CATEGORY.matcher(line).matches()) {
                categoryIndex = categories.size();
                categories.add(new CreditCategory(line.replaceAll("(?i)(\\s{0}==|==\\s{0})", "").strip()));
            } else if (line.startsWith("* ") && categoryIndex != -1) {
                categories.get(categoryIndex).addName(line.replaceAll("\\*\\s", ""));
            }
        }

        return new CreditFile(categories);
    }

    public static CreditFile fromJson(String json) {
        List<CreditCategory> categories = new ArrayList<>();

        JsonObject object = JsonObject.readHjson(json).asObject();

        JsonObject categoriesBlock = object.get("categories").asObject();
        List<String> categoryNames = categoriesBlock.names();

        for (String categoryName : categoryNames) {
            categories.add(new CreditCategory(categoryName));

            JsonArray names = categoriesBlock.get(categoryName).asArray();

            for (JsonValue jsonValue : names) {
                String name = jsonValue.asString();
                categories.get(categories.size() - 1).addName(name);
            }
        }

        return new CreditFile(categories);
    }

}
