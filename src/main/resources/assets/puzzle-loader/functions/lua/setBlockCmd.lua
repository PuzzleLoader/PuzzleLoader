local BlockUtil = require "assets.puzzle-loader.bindings.BlockUtil"
local CommandUtil = require "assets.puzzle-loader.bindings.CommandUtil"
local CosmicReach = require "assets.puzzle-loader.bindings.CosmicReach"

local dispatcher = CommandUtil:getCommandDispatcher()
local literal = CommandUtil:makeLiteral("setBlockA")

local argX = CommandUtil:makeIntArgument("x")
local argY = CommandUtil:makeIntArgument("y")
local argZ = CommandUtil:makeIntArgument("z")
local blockId = CommandUtil:makeGreedyStringArgument("id")

blockId:executes(CommandUtil:closureToCommand(
function (context)
            local x = CommandUtil:getIntArgument(context, "x")
            local y = CommandUtil:getIntArgument(context, "y")
            local z = CommandUtil:getIntArgument(context, "z")
            local id = CommandUtil:getStringArgument(context, "id")

            local world = CosmicReach:getWorld()

            BlockUtil:setBlockState(
                    world:getDefaultZone(),
                    BlockUtil:getBlockState(id),
                    x,
                    y,
                    z
            )
            return 0
        end
))

CommandUtil:_thenL(argZ, blockId)
CommandUtil:_thenL(argY, argZ)
CommandUtil:_thenL(argX, argY)
CommandUtil:_thenL(literal, argX)

dispatcher:register(literal)