# Puzzle Loader - CosmicAPI
This is a little module of puzzle loader with a purpose of having interfaces/overlays of cosmic reach classes. Each class **must** have the prefix `IPuzzle` if it is an interface of a cosmic reach class, and there must be a backend implementation of each interface for each version starting from 0.3.23 and beyond. Backends may be copied from previous versions or even deprecated if they are atleast 4 versions old, if the backend is within 1-3 versions old is must still be updated along with the current version.

- Transforming the API for use in the backend run `buildsrc/buildAPI`
- Building the backend for use as a mod run `buildsrc/buildBackend-{current_cr_version}`