package com.github.puzzle.game.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ILoadingEngine<SELF_TYPE extends ILoadingEngine<?>> {

    <STAGE_TYPE extends ILoadingStage<SELF_TYPE>> void addStage(STAGE_TYPE stage);

    void init();

    static <T extends ILoadingEngine<?>> Logger getLogger(Class<T> clazz) {
        return LoggerFactory.getLogger("Puzzle | " + clazz.getSimpleName());
    }

}
