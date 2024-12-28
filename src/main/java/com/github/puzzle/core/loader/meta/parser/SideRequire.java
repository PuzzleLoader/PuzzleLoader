package com.github.puzzle.core.loader.meta.parser;

import com.github.puzzle.core.loader.meta.EnvType;

public record SideRequire(
        boolean client,
        boolean server
) {

    public static final SideRequire CLIENT_ONLY = new SideRequire(true, false);
    public static final SideRequire SERVER_ONLY = new SideRequire(false, true);
    public static final SideRequire BOTH_REQUIRED = new SideRequire(true, true);
    public static final SideRequire SIDE_DOES_NOT_MATTER = new SideRequire(false, false);

    public boolean isClientOnly() {
        return client && !server;
    }

    public boolean isServerOnly() {
        return !client && server;
    }

    public boolean isBothRequired() {
        return client && server;
    }

    public boolean isNuhUhSided() {
        return !client && !server;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SideRequire s)
            return client == s.client && server == s.server;
        return false;
    }

    public boolean isAllowed(EnvType env) {
        if (isNuhUhSided() || isBothRequired()) return true;

        return switch (env) {
            case UNKNOWN -> true;
            case CLIENT -> isClientOnly();
            case SERVER -> isServerOnly();
        };
    }
}
