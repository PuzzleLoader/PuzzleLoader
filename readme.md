# Puzzle Loader

The new and improved modloader for Cosmic Reach.

### Credits
- [Nanobass](https://github.com/Nanobass), for the Flux-Entites & Flux v6/7 internals that puzzle uses as its api (with a few changes ofc)
- [CPW](https://github.com/cpw), for the base functionalities of [LegacyLauncher aka LaunchWrapper](https://github.com/Mojang/LegacyLauncher) used before it was refactored into oblivion.

## Adding to your projects
look at [jitpack](https://jitpack.io/#PuzzleLoader/PuzzleLoader) for adding this to your project or look at the example mods </br>
[Java Example Mod](https://github.com/PuzzleLoader/ExampleMod) </br>
[Kotlin Example Mod](https://github.com/PuzzleLoader/KotlinExampleMod) </br>
[Scala Example Mod](https://github.com/PuzzleLoader/ScalaExampleMod) </br>
[Lua Example Mod](https://github.com/PuzzleLoader/LuaExampleMod) </br>

[Need help set up your mod?](https://github.com/PuzzleLoader/PuzzleLoader/wiki/How-to-set-up-your-mod!)

### Contribution
- If you were to contribute to this project you must acknowledge that you are under the [LGPLv3](LICENSE.txt) License.
- We use the 4-space tabs provided by the code editor.
- To make a pull request you must provide a description of what you have changed in the pull request.
- If you are to make a mixin that is labeled as an accessor put it in the `src/main/resources/accessors.mixins.json` config, if not put it in the `src/main/resources/internal.mixins.json`, and if it fixes a game bug put it in `src/main/resources/bugfixes.mixins.json`
