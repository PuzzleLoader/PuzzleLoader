function registerBasicCommand(name, func)
    local dispatcher = CosmicReach:getCommandDispatcher()
    local literal = CosmicReach:getCmdLiteral(name)

    literal:executes(CosmicReach:functionToCommand(func))
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