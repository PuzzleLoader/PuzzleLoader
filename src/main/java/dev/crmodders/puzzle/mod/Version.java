package dev.crmodders.puzzle.mod;

public record Version(
        int Major,
        int Minor,
        int Patch
) {

    public static Version parseVersion(String ver) {
        String[] pieces = ver.split("\\.");
        return new Version(
                Integer.parseInt(pieces[0].replaceAll("^[0-9]", "")),
                Integer.parseInt(pieces[1].replaceAll("^[0-9]", "")),
                Integer.parseInt(pieces[2].replaceAll("^[0-9]", ""))
        );
    }

    @Override
    public String toString() {
        return "%d.%d.%d".formatted(Major, Minor, Patch);
    }
}
