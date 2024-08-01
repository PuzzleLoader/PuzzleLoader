local BlockUtil = require "assets.puzzle-loader.bindings.BlockUtil"
local CommandUtil = require "assets.puzzle-loader.bindings.CommandUtil"
local CosmicReach = require "assets.puzzle-loader.bindings.CosmicReach"

function registerBasicCommand(name, func)
    local dispatcher = CommandUtil:getCommandDispatcher()
    local literal = CommandUtil:makeLiteral(name)

    literal:executes(CommandUtil:closureToCommand(func))
    dispatcher:register(literal)
end

function setBlock(x, y, z, id)
    local world = CosmicReach:getWorld()

    BlockUtil:setBlockState(
        world:getDefaultZone(),
        BlockUtil:getBlockState(id),
        x,
        y,
        z
    )
end