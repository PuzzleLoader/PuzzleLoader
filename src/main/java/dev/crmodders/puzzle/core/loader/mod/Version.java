package dev.crmodders.puzzle.core.loader.mod;

public record Version(
        int Major,
        int Minor,
        int Patch
) {

    public static Version parseVersion(String ver) {
        String[] pieces = ver.split("\\.");
        return new Version(
                Integer.parseInt(pieces[0]),
                Integer.parseInt(pieces[1]),
                Integer.parseInt(pieces[2])
        );
    }

    public enum SIZE_COMP {
        SAME,
        LARGER,
        SMALLER
    }

    public SIZE_COMP otherIs(Version version) {
        if (Major == version.Major && Minor == version.Minor && Patch == version.Patch) return SIZE_COMP.SAME;
        if (Major > version.Major()) return SIZE_COMP.LARGER;
        if (Minor > version.Minor() && Major <= version.Major) return SIZE_COMP.LARGER;
        if (Patch > version.Patch() && Major <= version.Major && Minor <= version.Minor) return SIZE_COMP.LARGER;
        return SIZE_COMP.SMALLER;
    }

    @Override
    public String toString() {
        return "%d.%d.%d".formatted(Major, Minor, Patch);
    }
}
