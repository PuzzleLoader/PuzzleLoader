package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

public class CharDataAttribute implements DataTag.DataTagAttribute<Character> {

    char data;

    public CharDataAttribute(char data) {
        this.data = data;
    }

    public CharDataAttribute(Character data) {
        this.data = data;
    }

    @Override
    public void setValue(Character value) {
        this.data = value;
    }

    @Override
    public Character getValue() {
        return data;
    }

}
