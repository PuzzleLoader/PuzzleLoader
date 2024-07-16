package dev.crmodders.puzzle.game.blockentities;

import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.game.blockentities.interfaces.ITickable;

public class TickingBlockEntity extends ExtendedBlockEntity implements ITickable {

    public TickingBlockEntity(Identifier id, int globalX, int globalY, int globalZ) {
        super(id, globalX, globalY, globalZ);
    }

    @Override
    public void onTick(float tps) {

    }
}
