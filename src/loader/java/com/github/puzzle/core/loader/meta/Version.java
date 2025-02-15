package com.github.puzzle.core.loader.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;


//TODO: Possibly support things like release candidate?
public record Version(int Major, int Minor, int Patch, VersionType Type) {

    public enum VersionType {
        ALPHA,
        BETA,
        UNDEFINED
    }

    public Version(int Major, int Minor, int Patch){
        this(Major, Minor, Patch, VersionType.UNDEFINED);
    }
    public static @NotNull VersionType getVersionType(@NotNull String type){
        return switch (type.toLowerCase()) {
            case "beta" -> VersionType.BETA;
            case "alpha" -> VersionType.ALPHA;
            default -> VersionType.UNDEFINED;
        };
    }
    public static @NotNull Version parseVersion(String ver) {
        try {
            return parseVersionWithThrow(ver);
        }
        catch (NumberFormatException e){
            LoggerFactory.getLogger("Puzzle | Version parsing").error("Can't parse version");
            return new Version(0,0,0);
        }
    }

    @Contract("_ -> new")
    public static @NotNull Version parseVersionWithThrow(String ver) throws NumberFormatException {
        if (ver == null || ver.isBlank() || ver.isEmpty()) return new Version(0, 0, 0);
        String[] split =  ver.split("-", 2);
        String versionString;
        VersionType versionType = VersionType.UNDEFINED;

        if (split.length > 1) {
            versionString = split[0];
            versionType = getVersionType(split[1]);

        }
        else {
            versionString = ver;
        }
        String[] pieces = versionString.split("\\.");
        if (pieces.length > 3) {
            throw new NumberFormatException();
        }
        return new Version(
                Integer.parseInt(pieces[0]),
                Integer.parseInt(pieces[1]),
                Integer.parseInt(pieces[2]),
                versionType
        );
    }

    public enum SIZE_COMP {
        SAME,
        LARGER,
        SMALLER
    }

    public SIZE_COMP otherIs(@NotNull Version version) {
        if (Major == version.Major && Minor == version.Minor && Patch == version.Patch && version.Type.ordinal() == Type.ordinal()) return SIZE_COMP.SAME;
        if (Type.ordinal() > version.Type.ordinal()) return SIZE_COMP.LARGER;
        if (Major > version.Major && Type.ordinal() == version.Type.ordinal()) return SIZE_COMP.LARGER;
        if (Minor > version.Minor && Major == version.Major && Type.ordinal() == version.Type.ordinal()) return SIZE_COMP.LARGER;
        if (Patch > version.Patch && Major == version.Major && Minor == version.Minor && Type.ordinal() == version.Type.ordinal()) return SIZE_COMP.LARGER;
        return SIZE_COMP.SMALLER;
    }

    @Override
    public String toString() {
        return "%d.%d.%d%s".formatted(Major, Minor, Patch,Type == VersionType.UNDEFINED ? "" : "-" + Type);
    }
}
