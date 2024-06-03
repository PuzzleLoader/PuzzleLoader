# Puzzle Loader

The new and improved modloader for Cosmic Reach.

### Credits
- [Nanobass](https://github.com/Nanobass), for the Flux-Entites & Flux v6 internals that puzzle uses as its api (with a few changes ofc)


- [Mojang](https://github.com/Mojang), for the base functionalities of [LegacyLauncher aka LaunchWrapper](https://github.com/Mojang/LegacyLauncher) used before it was refactored into oblivion.

### Contribution
- If you were to contribute to this project you must acknollage that you are under the [Polyform Perimeter](LICENSE.md) License.
- We use the 4-space tabs provided by the code editor.
- To make a pull request you must provide a description of what you have changed in the pull request.
- If you are to make a mixin that is labeled as an accessor put it in the `src/main/resources/accessors.mixins.json` config, if not put it in the `src/main/resources/internal.mixins.json`, and if it fixes a game bug put it in `src/main/resources/bugfixes.mixins.json`