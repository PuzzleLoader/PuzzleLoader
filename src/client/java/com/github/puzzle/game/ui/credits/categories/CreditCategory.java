package com.github.puzzle.game.ui.credits.categories;

import java.util.ArrayList;
import java.util.List;

public class CreditCategory implements ICreditElement {

    String title;
    List<String> names;

    public CreditCategory(String name) {
        this.title = name;
        this.names = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void addName(String name) {
        names.add(name);
    }
    public List<String> getNames() {
        return names;
    }

}
