package com.github.puzzle.game.items.puzzle;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.keybindings.PuzzleControlSettings;
import com.github.puzzle.game.util.BlockSelectionUtil;
import com.github.puzzle.game.worldgen.schematics.Schematic;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.util.Identifier;

import static com.github.puzzle.core.Constants.MOD_ID;

public class BuilderWand implements IModItem {

    WANDMODES wandmodes = WANDMODES.SELECTPOS;
    Identifier id = Identifier.of(MOD_ID, "builder_wand");
    DataTagManifest tagManifest = new DataTagManifest();
    public static Vector3 pos1 = null;
    public static Vector3 pos2 = null;
    public static Schematic clipBoard;
    boolean nextPos = false;

    public BuilderWand() {
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(Identifier.of(MOD_ID, "baby_wand.png")));
        tagManifest.addTag(IModItem.IS_DEBUG_ATTRIBUTE.createTag(true));
    }

    @Override
    public void use(ItemSlot slot, Player player, boolean isLeftClick) {
        if(isLeftClick) return;
        if(PuzzleControlSettings.keyCrouch.isPressed()){
            //GameSingletons.openBlockEntityScreen(player, player.getZone(GameSingletons.world), this);
            int size = WANDMODES.values().length;
            if(wandmodes.ordinal() == size - 1) wandmodes = WANDMODES.SELECTPOS;
            else wandmodes = WANDMODES.values()[wandmodes.ordinal()+1];
            Chat.MAIN_CHAT.addMessage(GameSingletons.world, player, null, "Mode: "+ wandmodes.mode);
            return;
        }
        switch (wandmodes) {
            case SELECTPOS -> {
                setBlockPos(player);
            }
            case PASTE -> {
                pasteClipBoard(player);
            }
        }
    }

    private void pasteClipBoard(Player player) {
        if(clipBoard == null) {
            Chat.MAIN_CHAT.addMessage(GameSingletons.world, player, null, "clipBoard is null, run /gs");
            return;
        }
        BlockPosition blockPosition = BlockSelectionUtil.getBlockPositionLookingAt();
        if(blockPosition == null) return;
        Schematic.genSchematicStructureAtGlobal(clipBoard, player.getZone(), player.getChunk(GameSingletons.world), blockPosition.getGlobalX(), blockPosition.getGlobalY(), blockPosition.getGlobalZ());
    }

    private void setBlockPos(Player player) {
        BlockPosition position = BlockSelectionUtil.getBlockPositionLookingAt();
        if(position == null) return;
        Vector3 vector3 = new Vector3(position.getGlobalX(), position.getGlobalY(), position.getGlobalZ());
        if(nextPos) {
            pos1 = vector3;
            nextPos = false;
            Chat.MAIN_CHAT.addMessage(GameSingletons.world, player, null, "Pos1: "+ pos1);
        } else {
            pos2 = vector3;
            nextPos = true;
            Chat.MAIN_CHAT.addMessage(GameSingletons.world, player, null, "Pos2:" + pos2);
        }
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public String toString() {
        return "Item: " + getID();
    }

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public DataTagManifest getTagManifest() {
        return tagManifest;
    }

    @Override
    public String getName() {
        return "Schematic Wand";
    }

    public enum WANDMODES {
        SELECTPOS("select-positions"),
        PASTE("paste");

        private final String mode;

        WANDMODES(String versionName){
            this.mode = versionName;
        }

        public static WANDMODES getMode(String str){
            return switch (str) {
                case "Select Positions" -> SELECTPOS;
                case "Paste" -> PASTE;
                default -> SELECTPOS;
            };
        }
    }
}
