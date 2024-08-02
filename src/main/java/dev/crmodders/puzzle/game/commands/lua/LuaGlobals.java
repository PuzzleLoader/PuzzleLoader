package dev.crmodders.puzzle.game.commands.lua;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import dev.crmodders.puzzle.core.resources.ResourceLocation;
//import dev.crmodders.puzzle.util.AnsiColours;
//import finalforeach.cosmicreach.io.SaveLocation;
//import org.luaj.vm2.Globals;
//import org.luaj.vm2.LoadState;
//import org.luaj.vm2.LuaValue;
//import org.luaj.vm2.compiler.LuaC;
//import org.luaj.vm2.lib.*;
//import org.luaj.vm2.lib.jse.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class LuaGlobals {
//
//    public static Globals globals = LuaGlobals.getGlobals();
//    private static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | LuaGlobals");
//
//    public static Globals getGlobals() {
//        Globals var0 = new Globals();
//        var0.load(new JseBaseLib());
//        var0.load(new PackageLib());
//        var0.load(new Bit32Lib());
//        var0.load(new TableLib());
//        var0.load(new StringLib());
//        var0.load(new CoroutineLib());
//        var0.load(new JseMathLib());
//        var0.load(new JseIoLib());
//        var0.load(new JseOsLib());
//        var0.load(new LuajavaLib());
//
//        var0.set("loadScript", new OneArgFunction() {
//            @Override
//            public LuaValue call(LuaValue luaValue) {
//                ResourceLocation location = ResourceLocation.fromString(luaValue.checkjstring());
//
//                FileHandle classpathLocationFile = Gdx.files.classpath("assets/%s/%s.lua".formatted(location.namespace, location.name));
//                if (classpathLocationFile.exists()) {
//                    LOGGER.info("Loading " + AnsiColours.PURPLE + "\"{}\"" + AnsiColours.WHITE + " from Java Mod " + AnsiColours.GREEN + "\"{}\"" + AnsiColours.WHITE, location.name, location.namespace);
//                    return globals.get("require").call("assets/%s/%s".formatted(location.namespace, location.name));
//                }
//
//                FileHandle modLocationFile = Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/" + location.name + ".lua");
//                if (modLocationFile.exists()) {
//                    LOGGER.info("Loading " + AnsiColours.CYAN+"\"{}\"" + AnsiColours.WHITE + " from Mods Folder", location.name);
//                    return globals.get("require").call(SaveLocation.getSaveFolderLocation() + "/mods/assets/" + location.name);
//                }
//
//                FileHandle vanillaLocationFile = Gdx.files.internal(location.name + ".lua");
//                if (vanillaLocationFile.exists()) {
//                    LOGGER.info("Loading " + AnsiColours.YELLOW + "\"{}\""+AnsiColours.WHITE+" from Cosmic Reach", location.name);
//                    return globals.get("require").call(location.name);
//                }
//
//                LOGGER.error("Cannot find the resource {}", location);
//                return null;
//            }
//        });
//        var0.set("LCosmicReachUtil", CoerceJavaToLua.coerce(new LCosmicReachUtil()));
//        var0.set("LBlockUtil", CoerceJavaToLua.coerce(new LBlockUtil()));
//        var0.set("LCommandUtil", CoerceJavaToLua.coerce(new LCommandUtil()));
//
//        LoadState.install(var0);
//        LuaC.install(var0);
//        return var0;
//    }
//
//}
