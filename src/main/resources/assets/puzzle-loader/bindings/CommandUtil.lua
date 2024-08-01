---
--- Created by Mr Zombii:
--- DateTime: 7/31/2024 7:33 PM
---

local CommandUtil = {}

-- returns CommandDispatcher<PuzzleCommandSource>
function CommandUtil:getCommandDispatcher()
    return LCommandUtil:getCommandDispatcher()
end

-- name: String,
-- returns LiteralArgumentBuilder<PuzzleCommandSource>
function CommandUtil:makeLiteral(name)
    return LCommandUtil:makeLiteral(name)
end

-- func: LuaClosure,
-- returns: LuaValue
function CommandUtil:closureToCommand(func)
    return LCommandUtil:closureToCommand(func)
end

-- Lua Limitation Fixers

-- builder: ArgumentBuilder<PuzzleCommandSource, ?>,
-- argument: ArgumentBuilder<PuzzleCommandSource, ?>,
-- returns: ArgumentBuilder<PuzzleCommandSource, ?>
function CommandUtil:_thenL(builder, argument)
    print(builder, argument)
    return LCommandUtil:_thenL(builder, argument)
end

-- builder: ArgumentBuilder<PuzzleCommandSource, ?>,
-- argument: CommandNode<PuzzleCommandSource>,
-- returns: ArgumentBuilder<PuzzleCommandSource, ?>
function CommandUtil:_thenN(builder, argument)
    return LCommandUtil:_thenN(builder, argument)
end

-- builder: ArgumentBuilder<PuzzleCommandSource, ?>,
-- func: LuaClosure
-- :::: arguments of types -> ArgumentBuilder<PuzzleCommandSource, ?>
-- returns: ArgumentBuilder<PuzzleCommandSource, ?>
function CommandUtil:cmdWithArgs(builder, func, ...)
    --return CommandUtil:cmdWithArgs(builder, argument)
end

-- Boolean Argument

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Boolean>
function CommandUtil:makeBoolArgument(name)
    return LCommandUtil:makeBoolArgument(name)
end

-- commandContext: CommandContext<PuzzleCommandSource>,
-- name: String,
-- returns: Boolean
function CommandUtil:getBoolArgument(commandContext, name)
    return LCommandUtil:getBoolArgument(commandContext, name)
end

-- Double Argument

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Double>
function CommandUtil:makeDoubleArgument(name)
    return LCommandUtil:makeIntArgument(name)
end

-- name: String,
-- min: Double,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Double>
function CommandUtil:makeDoubleArgumentM(name, min)
    return LCommandUtil:makeDoubleArgumentM(name, min)
end

-- name: String,
-- min: Double,
-- max: Double,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Double>
function CommandUtil:makeDoubleArgumentMM(name, min, max)
    return LCommandUtil:makeDoubleArgumentMM(name, min, max)
end

-- commandContext: CommandContext<PuzzleCommandSource>,
-- name: String,
-- returns: Double
function CommandUtil:getDoubleArgument(commandContext, name)
    return LCommandUtil:getDoubleArgument(commandContext, name)
end

-- Float Argument

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Float>
function CommandUtil:makeFloatArgument(name)
    return LCommandUtil:makeIntArgument(name)
end

-- name: String,
-- min: Float,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Float>
function CommandUtil:makeFloatArgumentM(name, min)
    return LCommandUtil:makeFloatArgumentM(name, min)
end

-- name: String,
-- min: Float,
-- max: Float,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Float>
function CommandUtil:makeFloatArgumentMM(name, min, max)
    return LCommandUtil:makeFloatArgumentMM(name, min, max)
end

-- commandContext: CommandContext<PuzzleCommandSource>,
-- name: String,
-- returns: Float
function CommandUtil:getFloatArgument(commandContext, name)
    return LCommandUtil:getFloatArgument(commandContext, name)
end

-- Long Argument

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Boolean>
function CommandUtil:makeLongArgument(name)
    return LCommandUtil:makeIntArgument(name)
end

-- name: String,
-- min: Long,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Boolean>
function CommandUtil:makeLongArgumentM(name, min)
    return LCommandUtil:makeLongArgumentM(name, min)
end

-- name: String,
-- min: Long,
-- max: Long,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Boolean>
function CommandUtil:makeLongArgumentMM(name, min, max)
    return LCommandUtil:makeLongArgumentMM(name, min, max)
end

-- commandContext: CommandContext<PuzzleCommandSource>,
-- name: String,
-- returns: Long
function CommandUtil:getLongArgument(commandContext, name)
    return LCommandUtil:getLongArgument(commandContext, name)
end

-- Integer Argument

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Boolean>
function CommandUtil:makeIntArgument(name)
    return LCommandUtil:makeIntArgument(name)
end

-- name: String,
-- min: Integer,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Boolean>
function CommandUtil:makeIntArgumentM(name, min)
    return LCommandUtil:makeIntArgumentM(name, min)
end

-- name: String,
-- min: Integer,
-- max: Integer,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, Boolean>
function CommandUtil:makeIntArgumentMM(name, min, max)
    return LCommandUtil:makeIntArgumentMM(name, min, max)
end

-- commandContext: CommandContext<PuzzleCommandSource>,
-- name: String,
-- returns: Integer
function CommandUtil:getIntArgument(commandContext, name)
    return LCommandUtil:getIntArgument(commandContext, name)
end


-- String Argument

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, String>
function CommandUtil:makeStringArgument(name)
    return LCommandUtil:makeStringArgument(name)
end

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, String>
function CommandUtil:makeWordArgument(name)
    return LCommandUtil:makeWordArgument(name)
end

-- name: String,
-- returns RequiredArgumentBuilder<PuzzleCommandSource, String>
function CommandUtil:makeGreedyStringArgument(name)
    return LCommandUtil:makeGreedyStringArgument(name)
end

-- commandContext: CommandContext<PuzzleCommandSource>,
-- name: String,
-- returns: String
function CommandUtil:getStringArgument(commandContext, name)
    return LCommandUtil:getStringArgument(commandContext, name)
end

return CommandUtil