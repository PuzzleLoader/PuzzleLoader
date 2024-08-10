package com.github.puzzle.game.oredict.tags;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple class like the Identifier but just for compiling a groups of items and blocks
 * @see com.github.puzzle.core.Identifier
 * @author Mr Zombii
 */
public class Tag {

    static final Map<String, Tag> tagRegistry = new HashMap<>();
    final String name;

    Tag(String name) {
        this.name = name;
        tagRegistry.put(name, this);
    }

    /**
     * @return the name of the tag
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * @param name the tag name you are checking.
     * @return the output that says if it exists or not.
     */
    public static boolean tagExist(String name) {
        return tagRegistry.containsKey(name);
    }

    /**
     * @param tag a name of a tag that may or may not exist already
     * @return a Tag that was either gotten by reference or created depending on its existence by its name
     */
    public static Tag of(String tag) {
        if (!tag.replaceAll("[a-z_]", "").isEmpty())
            throw new TagFormatException("Tag \""+tag+"\" is formatted incorrectly and can only contain lowercase letters and underscores.");

        return tagRegistry.containsKey(tag) ? tagRegistry.get(tag) : new Tag(tag);
    }

}
