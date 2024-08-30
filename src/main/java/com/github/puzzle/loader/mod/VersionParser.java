package com.github.puzzle.loader.mod;

import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import static com.github.puzzle.loader.mod.Version.parseVersionWithThrow;

public class VersionParser {
    //TODO: add ability to parse version ranges
    public static boolean hasDependencyVersion(@NotNull Version current, @NotNull String wanted){

        if(wanted.isEmpty() || wanted.isBlank())
            throw new RuntimeException("Invalid dependency version string");

        if(wanted.equals("*"))
            return true;
        try {
            return current.otherIs(parseVersionWithThrow(wanted)) == Version.SIZE_COMP.SAME;
        }
        catch (NumberFormatException e){
            try {
                if (wanted.startsWith(">=")) {
                    wanted = wanted.replace(">=", "");

                    Version.SIZE_COMP check = current.otherIs(Version.parseVersionWithThrow(wanted));

                    return check == Version.SIZE_COMP.SAME || check == Version.SIZE_COMP.LARGER;
                } else if (wanted.startsWith(">")) {
                    wanted = wanted.replace(">", "");
                    Version.SIZE_COMP check = current.otherIs(Version.parseVersionWithThrow(wanted));
                    return check == Version.SIZE_COMP.LARGER;
                }
            }
            catch (NumberFormatException e2) {
                LogManager.getLogger("Version Parser").fatal("Version is invalid or format is not support");
                return false;
            }
            LogManager.getLogger("Version Parser").fatal("Version is invalid or format is not support");
            return false;
        }

    }
}
