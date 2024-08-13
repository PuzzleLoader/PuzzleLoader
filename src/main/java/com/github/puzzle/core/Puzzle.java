package com.github.puzzle.core;

import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.Globals;
import com.github.puzzle.game.engine.shaders.ItemShader;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.impl.BasicItem;
import com.github.puzzle.game.items.impl.BasicTool;
import com.github.puzzle.game.items.puzzle.BlockWrench;
import com.github.puzzle.game.items.puzzle.CheckBoard;
import com.github.puzzle.game.items.puzzle.NullStick;
import com.github.puzzle.game.oredict.tags.BuiltInTags;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PostModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PreModInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.blocks.Block;

import java.io.IOException;
import java.io.InputStream;

public class Puzzle implements PreModInitializer, ModInitializer, PostModInitializer {
    public static final String MOD_ID = "puzzle-loader";
    public static final String VERSION;

    static {
        try {
            InputStream stream = getFile("assets/puzzle-loader/version.txt");
            String bytez = new String(stream.readAllBytes()).strip();
            stream.close();
            if (!bytez.contains(".")) {
                VERSION = "69.69.69";
            } else VERSION = bytez;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getFile(String file) {
        InputStream input = Puzzle.class.getResourceAsStream(file);
        if (input == null) {
            input = PuzzleClassLoader.class.getClassLoader().getResourceAsStream(file);
        }
        return input;
    }

    @Override
    public void onPreInit() {
        try {
            ILanguageFile lang = LanguageFileVersion1.loadLanguageFromString(new String(getFile(Globals.LanguageEnUs.toPath()).readAllBytes()));
            LanguageManager.registerLanguageFile(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IModItem DebugStick;
    public static IModItem CheckerBoard;
    public static IModItem BlockWrench;

    @Override
    public void onInit() {
        Threads.runOnMainThread(ItemShader::initItemShader);

        DebugStick = IModItem.registerItem(new NullStick());
        CheckerBoard = IModItem.registerItem(new CheckBoard());
        BlockWrench = IModItem.registerItem(new BlockWrench());
    }

    @Override
    public void onPostInit() {
        BuiltInTags.stone.add(Block.getInstance("block_stone_basalt"));
        BuiltInTags.stone.add(Block.getInstance("block_stone_gabbro"));
        BuiltInTags.stone.add(Block.getInstance("block_stone_limestone"));

        BuiltInTags.glass.add(Block.getInstance("block_glass"));

        BuiltInTags.grass.add(Block.getInstance("block_grass"));
        BuiltInTags.dirt.add(Block.getInstance("block_dirt"));
        BuiltInTags.dirt.add(Block.getInstance("block_lunar_soil"));
        BuiltInTags.dirt.add(Block.getInstance("block_lunar_soil_packed"));

        BuiltInTags.aluminum_block.add(Block.getInstance("block_aluminium_panel"));
        BuiltInTags.steel_block.add(Block.getInstance("block_metal_panel"));

        BuiltInTags.logs.add(Block.getInstance("block_tree_log"));
        BuiltInTags.planks.add(Block.getInstance("block_wood_planks"));
        BuiltInTags.light.add(Block.getInstance("block_light"));
    }
}
