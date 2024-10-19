package com.github.puzzle.game.ui.credits.categories;

import java.util.ArrayList;
import java.util.List;

public class CreditCategory implements ICreditElement {

    String name;
    List<String> names;

    public CreditCategory(String name) {
        this.name = name;
        this.names = new ArrayList<>();
    }

    public String getCategoryName() {
        return name;
    }
    public void setCategoryName(String name) {
        this.name = name;
    }

    public void addName(String name) {
        names.add(name);
    }
    public List<String> getNames() {
        return names;
    }

}
