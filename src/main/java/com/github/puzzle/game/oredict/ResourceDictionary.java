package com.github.puzzle.game.oredict;

import com.github.puzzle.game.oredict.tags.Tag;
import finalforeach.cosmicreach.blocks.Block;
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
    static Map<Tag, Set<Block>> taggedBlocks = new HashMap<>();

    /**
     * @param tag A name that relates to a list of various blocks
     * @return A list of blocks related to the tag param
     */
    public static Block[] getBlocksFromTag(Tag tag) {
        return taggedBlocks.get(tag).toArray(new Block[0]);
    }
    /**
     * @param tag A name that relates to a list of various Items
     * @return A list of items related to the tag param
     */
    public static Item[] getItemFromTag(Tag tag) {
        return taggedItems.get(tag).toArray(new Item[0]);
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
    public static void addBlockToTag(Tag tag, Block block) {
        Set<Block> blocks = taggedBlocks.containsKey(tag) ? taggedBlocks.get(tag) : new HashSet<>();
        blocks.add(block);
        taggedBlocks.put(tag, blocks);
    }

}
