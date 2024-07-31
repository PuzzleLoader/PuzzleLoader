function registerBasicCommand(name, func)
    local dispatcher = CosmicReach:getCommandDispatcher()
    local literal = CosmicReach:getCmdLiteral(name)

    literal:executes(CosmicReach:functionToCommand(func))
    dispatcher:register(literal)
end

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