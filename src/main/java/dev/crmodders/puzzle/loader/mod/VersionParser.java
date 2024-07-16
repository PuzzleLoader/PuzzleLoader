package dev.crmodders.puzzle.loader.mod;

import org.apache.logging.log4j.LogManager;

public class VersionParser {

    //TODO:add ability to parse version ranges
    public static boolean hasDependencyVersion(Version current, String wanted){

        if(wanted.isEmpty() || wanted.isBlank()){
            throw new RuntimeException("Invalid dependency version string");
        }
        if(wanted.equals("*"))
           return true;
        var dashSplit = wanted.split("-",2);
        String versionString;
        if (dashSplit.length > 1) {
            //IDEA: check for b,a,beta,alpha?
            versionString = dashSplit[0];
        }
        else {
            versionString = wanted;
        }

        try {
            return current.otherIs(Version.parseVersionWithThrow(versionString)) == Version.SIZE_COMP.SAME;
        }
        catch (NumberFormatException e){

            if (versionString.startsWith(">=")) {
                versionString = versionString.replace(">=", "");
                Version.SIZE_COMP check = current.otherIs(Version.parseVersionWithThrow(versionString));
                return check == Version.SIZE_COMP.SAME || check == Version.SIZE_COMP.LARGER;
            } else if (versionString.startsWith(">")) {
                versionString = versionString.replace(">", "");
                Version.SIZE_COMP check = current.otherIs(Version.parseVersionWithThrow(versionString));
                return check == Version.SIZE_COMP.LARGER;
            }
            LogManager.getLogger().fatal("Version is invalid or format is not support");
            return false;

        }

    }

}
