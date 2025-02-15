package com.github.puzzle.core.loader.meta.parser;

import com.github.puzzle.core.loader.meta.Version;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import static com.github.puzzle.core.loader.meta.Version.parseVersionWithThrow;

public class VersionParser {
    public static boolean hasDependencyVersion(@NotNull Version current, @NotNull String wanted){

        if(wanted.isEmpty() || wanted.isBlank()) {
            throw new RuntimeException("Invalid dependency version string");

        }
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
                }
                if (wanted.startsWith(">")) {
                    wanted = wanted.replace(">", "");
                    Version.SIZE_COMP check = current.otherIs(Version.parseVersionWithThrow(wanted));
                    return check == Version.SIZE_COMP.LARGER;
                }
                if (wanted.contains("...")) {
                    String[] wants = wanted.split("\\.\\.\\.");
                    if (wants.length == 2) {
                        Version.SIZE_COMP check1 = current.otherIs(Version.parseVersionWithThrow(wants[0]));
                        Version.SIZE_COMP check2 = current.otherIs(Version.parseVersionWithThrow(wants[1]));

                        return (check1 == Version.SIZE_COMP.SAME || check1 == Version.SIZE_COMP.LARGER) && (check2 == Version.SIZE_COMP.SAME || check2 == Version.SIZE_COMP.SMALLER);
                    }
                }
            }
            catch (NumberFormatException e2) {
                LogManager.getLogger("Version Parser").fatal("Version is invalid or format is not supported");
                return false;
            }
            LogManager.getLogger("Version Parser").fatal("Version is invalid or format is not supported");
            return false;
        }

    }
}
