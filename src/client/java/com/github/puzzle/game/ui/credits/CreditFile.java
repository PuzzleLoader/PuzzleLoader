package com.github.puzzle.game.ui.credits;

import com.github.puzzle.game.ui.credits.categories.ICreditElement;
import com.github.puzzle.game.ui.credits.categories.ListCredit;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class CreditFile {

    final Collection<? extends ICreditElement> categories;

    CreditFile(final Collection<? extends ICreditElement> categories) {
        this.categories = categories;
    }

    final static Pattern VANILLA_CATEGORY = Pattern.compile("(?i)==.+==");

    public static CreditFile fromVanilla(String txt) {
        List<ListCredit> categories = new ArrayList<>();

        int categoryIndex = -1;
        for (String line : txt.split("\n")) {
            line = line.strip();

            if (VANILLA_CATEGORY.matcher(line).matches()) {
                categoryIndex = categories.size();
                categories.add(new ListCredit(line.replaceAll("(?i)(\\s{0}==|==\\s{0})", "").strip()));
            } else if (line.startsWith("* ") && categoryIndex != -1) {
                categories.get(categoryIndex).addName(line.replaceAll("\\*\\s", ""));
            }
        }

        return new CreditFile(categories);
    }

    public static CreditFile fromJson(String json) {
        List<ICreditElement> categories = new ArrayList<>();

        JsonObject object = JsonObject.readHjson(json).asObject();

        JsonArray categoriesBlock = object.get("categories").asArray();

        for (JsonValue value : categoriesBlock) {
            JsonObject obj = value.asObject();

            Class<? extends ICreditElement> clazz = ICreditElement.TYPE_TO_ELEMENT.get(obj.getString("type", "list"));
            try {
                ICreditElement element = clazz.newInstance();
                element.fromJson(obj);
                categories.add(element);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return new CreditFile(categories);
    }

}
