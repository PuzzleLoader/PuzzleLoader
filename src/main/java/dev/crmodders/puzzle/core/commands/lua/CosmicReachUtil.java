package dev.crmodders.puzzle.core.commands.lua;

import com.badlogic.gdx.utils.Queue;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.crmodders.puzzle.core.commands.CommandManager;
import dev.crmodders.puzzle.core.commands.LuaCommand;
import dev.crmodders.puzzle.core.commands.PuzzleCommandSource;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class CosmicReachUtil {

    public Chat getChat() {
        return Chat.MAIN_CHAT;
    }

    public InGame getInGameMenu() {
        return InGame.IN_GAME;
    }

    public World getWorld() {
        return InGame.world;
    }

    public Player getLocalPlayer() {
        return InGame.getLocalPlayer();
    }

    // CommandUtil

    public CommandDispatcher<PuzzleCommandSource> getCommandDispatcher() {
        return CommandManager.dispatcher;
    }

    public LiteralArgumentBuilder<PuzzleCommandSource> getCmdLiteral(String literal) {
        return CommandManager.literal(literal);
    }

    public <T> RequiredArgumentBuilder<PuzzleCommandSource, T> setupCmdArgument(String literal, ArgumentType<T> type) {
        return CommandManager.argument(literal, type);
    }

    public LuaValue functionToCommand(LuaClosure value) {
        return CoerceJavaToLua.coerce(new LuaCommand(value));
    }

    public LiteralArgumentBuilder<PuzzleCommandSource> cthen(LiteralArgumentBuilder<PuzzleCommandSource> literal, ArgumentBuilder<PuzzleCommandSource, ?> argument) {
        return literal.then(argument);
    }

    public LiteralArgumentBuilder<PuzzleCommandSource> cthen(LiteralArgumentBuilder<PuzzleCommandSource> literal, CommandNode<PuzzleCommandSource> argument) {
        return literal.then(argument);
    }

    // BlockUtil

    public Block getBlock(String id) {
        return Block.getInstance(id);
    }

    public BlockState getBlockState(String id) {
        return BlockState.getInstance(id);
    }

    public void setBlockState(Zone zone, BlockState block, int x, int y, int z) {
        int cx = Math.floorDiv(x, 16);
        int cy = Math.floorDiv(y, 16);
        int cz = Math.floorDiv(z, 16);

        Chunk c = zone.getChunkAtChunkCoords(cx, cy, cz);
        if (c == null) {
            c = new Chunk(cx, cy, cz);
            c.initChunkData();
            zone.addChunk(c);
        }

        x -= 16 * cx;
        y -= 16 * cy;
        z -= 16 * cz;
        BlockSetter.replaceBlock(zone, block, new BlockPosition(c, x, y, z), new Queue<>());
    }

}
