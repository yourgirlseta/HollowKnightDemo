# Hollow Knight

A 2D computer graphics project inspired by *Hollow Knight*.

This project was developed as a university assignment at Sharif University of Technology using the libGDX framework. The project is intended as an educational exercise in game development, computer graphics, rendering, animation, and interactive application design.

> This is a student project inspired by *Hollow Knight* and is not affiliated with or endorsed by Team Cherry.

## Author

**Setareh Forouzan**

## Academic Information

- **University:** Sharif University of Technology
- **Project Type:** Computer Graphics Assignment
- **Framework:** libGDX
- **Language:** Java

## Project Structure
```text
hollowKnight/
├── assets/     # Game assets such as textures, sounds, and fonts
├── core/       # Shared game logic and application code
├── lwjgl3/     # Desktop launcher using LWJGL3
├── gradle/     # Gradle Wrapper files
├── build.gradle
├── settings.gradle
├── gradlew
└── gradlew.bat

### Modules

- `core`: Contains the main application logic shared across platforms.
- `lwjgl3`: Contains the desktop launcher and LWJGL3-specific configuration.
- `assets`: Contains the resources used by the game.

## Requirements

To run the project, install one of the following:

- Java Development Kit (JDK)
- No separate Gradle installation is required because the project includes the Gradle Wrapper.

## Running the Project

### Windows

bat
gradlew.bat lwjgl3:run

### Linux and macOS

bash
./gradlew lwjgl3:run

## Building the Project

To build all project modules:

### Windows

bat
gradlew.bat build

### Linux and macOS

bash
./gradlew build

The runnable desktop JAR file will be generated in:

text
lwjgl3/build/libs/

## Common Gradle Tasks

| Task | Description |
|------|-------------|
| `build` | Builds the sources and archives of all modules |
| `clean` | Removes generated build folders |
| `test` | Runs the available unit tests |
| `lwjgl3:run` | Starts the desktop application |
| `lwjgl3:jar` | Builds the runnable desktop JAR |
| `core:clean` | Removes the build folder of the `core` module |

## Useful Gradle Options

- `--continue`: Continues running tasks even if one task fails.
- `--daemon`: Uses the Gradle daemon to improve build performance.
- `--offline`: Uses cached dependencies without accessing the network.
- `--refresh-dependencies`: Revalidates and downloads project dependencies.

For example:

bash
./gradlew lwjgl3:run --offline

## Development Notes

The project was generated using [`gdx-liftoff`](https://github.com/libgdx/gdx-liftoff) and uses libGDX as its game development framework.

The desktop version runs through LWJGL3. The shared game code is located in the `core` module, which keeps the application logic separate from the platform-specific launcher.

## License

This project was created for educational purposes.


ાવી
