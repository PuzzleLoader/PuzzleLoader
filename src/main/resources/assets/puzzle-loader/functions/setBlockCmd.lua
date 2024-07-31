local intz = luajava.bindClass("com.mojang.brigadier.arguments.IntegerArgumentType")

function setBlock(x, y, z, id)
    local world = CosmicReach:getWorld()

    CosmicReach:setBlockState(
        world:getDefaultZone(),
        CosmicReach:getBlockState(id),
        x,
        y,
        z
    )
end

local dispatcher = CosmicReach:getCommandDispatcher()
local literal = CosmicReach:getCmdLiteral("setBlock")

CosmicReach:cthen(
literal,CosmicReach:cthen(
CosmicReach:cthen(CosmicReach:getCmdLiteral("x"), intz.integer())
, CosmicReach:cthen(CosmicReach:cthen(CosmicReach:getCmdLiteral("y"), intz.integer()), CosmicReach:cthen(CosmicReach:getCmdLiteral("y"), intz.integer():executes(CosmicReach:functionToCommand(function (context)
    print(context)
    context:getSource():getChat():sendMessage(
        context:getSource():getWorld(),
        context:getSource():getPlayer(),
        nil,
        "Hello World From Lua"
    )
    return 0;
end)))))
)

dispatcher:register(literal)