package dev.crmodders.puzzle.game.events;

import dev.crmodders.puzzle.game.block.IModBlock;
import dev.crmodders.puzzle.game.factories.IFactory;

import java.util.List;

public class OnRegisterBlockEvent {

    private final List<IFactory<IModBlock>> blockFactories;

    public OnRegisterBlockEvent(List<IFactory<IModBlock>> blockFactories) {
        this.blockFactories = blockFactories;
    }

    public void registerBlock(IFactory<IModBlock> blockFactory) {
        blockFactories.add(blockFactory);
    }

}