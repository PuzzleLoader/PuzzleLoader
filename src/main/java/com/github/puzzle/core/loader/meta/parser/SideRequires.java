package com.github.puzzle.core.loader.meta.parser;

import com.github.puzzle.core.loader.meta.EnvType;

public record SideRequires(
        boolean client,
        boolean server
) {

    public static final SideRequires CLIENT_ONLY = new SideRequires(true, false);
    public static final SideRequires SERVER_ONLY = new SideRequires(false, true);
    public static final SideRequires BOTH_REQUIRED = new SideRequires(true, true);
    public static final SideRequires SIDE_DOES_NOT_MATTER = new SideRequires(false, false);

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
        if (obj instanceof SideRequires s)
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
