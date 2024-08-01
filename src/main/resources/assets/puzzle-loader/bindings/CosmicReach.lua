---
--- Created by Mr Zombii:
--- DateTime: 7/31/2024 7:33 PM
---

local CosmicReach = {}

-- returns Chat
function CosmicReach:getChat()
    return LCosmicReachUtil:getChat()
end

-- returns InGame
function CosmicReach:getInGameMenu()
    return LCosmicReachUtil:getInGameMenu()
end

-- returns World
function CosmicReach:getWorld()
    return LCosmicReachUtil:getWorld()
end

-- returns Player
function CosmicReach:getLocalPlayer()
    return LCosmicReachUtil:getLocalPlayer()
end

return CosmicReach