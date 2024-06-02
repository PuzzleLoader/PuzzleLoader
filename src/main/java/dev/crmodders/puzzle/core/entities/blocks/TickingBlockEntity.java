package dev.crmodders.puzzle.core.entities.blocks;

import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.core.entities.blocks.interfaces.ITickable;

public class TickingBlockEntity extends ExtendedBlockEntity implements ITickable {

    public TickingBlockEntity(Identifier id, int globalX, int globalY, int globalZ) {
        super(id, globalX, globalY, globalZ);
    }

    @Override
    public void onTick(float tps) {

    }
}
