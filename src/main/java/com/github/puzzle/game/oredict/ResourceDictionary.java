package com.github.puzzle.game.oredict;

import com.github.puzzle.game.oredict.tags.Tag;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.items.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A similar concept to OreDictionary but has more of a purpose
 * <a href="https://docs.minecraftforge.net/en/1.12.x/utilities/oredictionary/">Ore Dict Reference</a>
 * @author Mr Zombii
 */
public class ResourceDictionary {

    static Map<Tag, Set<Item>> taggedItems = new HashMap<>();
    static Map<Tag, Set<BlockState>> taggedBlockStates = new HashMap<>();

    /**
     * @param tag A name that relates to a list of various blocks
     * @return A list of blocks related to the tag param
     */
    public static Set<BlockState> getBlockStatesFromTag(Tag tag) {
        return taggedBlockStates.get(tag);
    }

    /**
     * @param tag A name that relates to a list of various Items
     * @return A list of items related to the tag param
     */
    public static Set<Item> getItemsFromTag(Tag tag) {
        return taggedItems.get(tag);
    }

    /**
     * @param tag A name that will be a reference to the item
     * @param item An item that will be added to a list of other related item
     */
    public static void addItemToTag(Tag tag, Item item) {
        Set<Item> items = taggedItems.containsKey(tag) ? taggedItems.get(tag) : new HashSet<>();
        items.add(item);
        taggedItems.put(tag, items);
    }

    /**
     * @param tag A name that will be a reference to the block
     * @param block A block that will be added to a list of other related blocks
     */
    public static void addBlockStateToTag(Tag tag, BlockState block) {
        Set<BlockState> blocks = taggedBlockStates.containsKey(tag) ? taggedBlockStates.get(tag) : new HashSet<>();
        blocks.add(block);
        addItemToTag(tag, block.getItem());
        taggedBlockStates.put(tag, blocks);
    }

}
