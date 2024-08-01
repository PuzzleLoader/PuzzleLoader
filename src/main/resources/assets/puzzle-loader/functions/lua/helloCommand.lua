local CommandUtil = require "assets.puzzle-loader.bindings.CommandUtil"
--loadScript("puzzle-loader:bindings/CommandUtil")

local function registerBasicCommand(name, func)
    local dispatcher = CommandUtil:getCommandDispatcher()
    local literal = CommandUtil:makeLiteral(name)

    literal:executes(CommandUtil:closureToCommand(func))
    dispatcher:register(literal)
end

registerBasicCommand("hello",
function (context)
    print(context)
    context:getSource():getChat():sendMessage(
        context:getSource():getWorld(),
        context:getSource():getPlayer(),
        nil,
        "Hello World From Lua"
    )
    return 0;
end)