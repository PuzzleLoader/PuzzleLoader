package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

import java.util.List;

public class ListDataAttribute<T> implements DataTag.DataTagAttribute<List<T>> {

    List<T> data;

    public ListDataAttribute(List<T> data) {
        this.data = data;
    }

    @Override
    public void setValue(List<T> value) {
        this.data = value;
    }

    @Override
    public List<T> getValue() {
        return data;
    }

}
