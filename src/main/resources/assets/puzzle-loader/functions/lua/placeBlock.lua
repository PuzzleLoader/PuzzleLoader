local CosmicReach = require "assets.puzzle-loader.bindings.CosmicReach"
local BlockUtil = require "assets.puzzle-loader.bindings.BlockUtil"

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

local player = CosmicReach:getLocalPlayer()

local playerX = player:getEntity().position.x
local playerY = player:getEntity().position.y
local playerZ = player:getEntity().position.z

setBlock(playerX, playerY, playerZ, "base:aluminium_panel[default]")