package dev.crmodders.puzzle.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaCommand implements Command<PuzzleCommandSource> {

    LuaClosure function;

    public LuaCommand(LuaClosure func) {
        function = func;
    }

    @Override
    public int run(CommandContext<PuzzleCommandSource> commandContext) throws CommandSyntaxException {
        return function.call(CoerceJavaToLua.coerce(commandContext)).checkint();
    }
}
