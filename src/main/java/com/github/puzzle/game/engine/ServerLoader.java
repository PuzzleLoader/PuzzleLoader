package com.github.puzzle.game.engine;

import com.github.puzzle.game.ServerGlobals;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

public class ServerLoader implements ILoadingEngine<ServerLoader> {

    public static final Logger LOGGER = ILoadingEngine.getLogger(ServerLoader.class);

    private final Queue<ILoadingStage<ServerLoader>> STAGES = new LinkedList<>();

    public ServerLoader() {
        ServerGlobals.ENGINE = this;
    }

    @Override
    public <STAGE_TYPE extends ILoadingStage<ServerLoader>> void addStage(STAGE_TYPE stage) {
        stage.init(this);
        STAGES.add(stage);
    }

    @Override
    public void init() {

    }
}
