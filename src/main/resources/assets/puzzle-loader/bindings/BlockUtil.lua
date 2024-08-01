---
--- Created by Mr Zombii:
--- DateTime: 7/31/2024 7:33 PM
---

local BlockUtil = {}

-- id: String,
-- returns Block
function BlockUtil:getBlock(id)
    return LBlockUtil:getBlock(id)
end

-- id: String,
-- returns BlockState
function BlockUtil:getBlockState(id)
    return LBlockUtil:getBlockState(id)
end

-- zone: Zone,
-- block: BlockState,
-- x: Integer,
-- y: Integer,
-- z: Integer
function BlockUtil:setBlockState(zone, block, x, y, z)
    LBlockUtil:setBlockState(zone, block, x, y, z)
end

return BlockUtil