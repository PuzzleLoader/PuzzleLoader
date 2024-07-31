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

local player = CosmicReach:getLocalPlayer()

local playerX = player:getEntity().position.x
local playerY = player:getEntity().position.y
local playerZ = player:getEntity().position.z

setBlock(playerX, playerY, playerZ, "base:aluminium_panel[default]")