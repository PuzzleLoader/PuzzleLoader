package com.github.puzzle.game.tags;

import com.github.puzzle.game.CommonRegistries;
import com.github.puzzle.game.oredict.ResourceDictionary;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.util.Identifier;

/**
 * A simple class like the Identifier but just for compiling a groups of items and blocks
 * @see Identifier
 * @author Mr Zombii
 */
public class Tag {

    final String name;

    Tag(String name) {
        this.name = name;
        CommonRegistries.TAGS.store(Identifier.of(name), this);
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
        return CommonRegistries.TAGS.contains(Identifier.of(name));
    }

    /**
     * @param tag a name of a tag that may or may not exist already
     * @return a Tag that was either gotten by reference or created depending on its existence by its name
     */
    public static Tag of(String tag) {
        if (!tag.replaceAll("[a-z_]", "").isEmpty())
            throw new TagFormatException("Tag \""+tag+"\" is formatted incorrectly and can only contain lowercase letters and underscores.");

        return tagExist(tag) ? CommonRegistries.TAGS.get(Identifier.of(tag)) : new Tag(tag);
    }

    /**
     * @param item the item you want to add to the tag
     */
    public void add(Item item) {
        ResourceDictionary.addItemToTag(this, item);
    }

    /**
     * @param blockState the blockState you want to add to the tag
     */
    public void add(BlockState blockState) {
        ResourceDictionary.addBlockStateToTag(this, blockState);
    }

    /**
     * @param block the block you want to add to the tag
     */
    public void add(Block block) {
        for (BlockState state : block.blockStates.values())
            ResourceDictionary.addBlockStateToTag(this, state);
    }

}