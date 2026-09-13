# Dream Cakes 🎂

Dream Cakes is a modern Android application that showcases a delicious variety of cakes. It fetches
cake data from a remote API and presents it in a clean, user-friendly interface built with Jetpack
Compose and Material 3.

## 🚀 Features

- **Cake Catalog**: Browse a comprehensive list of cakes with their titles and detailed
  descriptions.
- **Rich Visuals**: High-quality images for each cake, loaded efficiently using Coil.
- **Modern UI**: A fully declarative UI built with Jetpack Compose, following Material 3 design
  guidelines.
- **State Management**: Robust handling of Loading, Success, and Error states for a seamless user
  experience.
- **Responsive Layout**: Designed to work gracefully across different screen sizes.

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android's modern
  toolkit for building native UI.
- **Theming**: [Material 3](https://m3.material.io/) - The latest evolution of Material Design.
- **Networking
  **: [Retrofit 2](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) -
  For type-safe REST API consumption.
- **JSON Parsing**: [Gson](https://github.com/google/gson) - For converting JSON to Kotlin objects.
- **Dependency Injection
  **: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Built on
  top of Dagger for simplified DI in Android.
- **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/) - An image loading library for
  Android backed by Kotlin Coroutines.
- **Architecture**: MVVM (Model-View-ViewModel) with a focus on clean separation of concerns.
- **Concurrency**: Kotlin Coroutines and Flow for asynchronous data handling.

## 🏗 Project Structure

The project follows a modular and layered architecture:

- **`data`**: Contains the API service definitions (`CakeApiService`) and data models (`CakeModel`).
- **`repository`**: Provides an abstraction layer for data operations, separating the UI from data
  sources.
- **`presentation`**: Houses the UI components, including:
    - **`screens`**: The main Compose screens (e.g., `CakeScreen`).
    - **`viewmodel`**: `CakesViewModel` for managing UI state and business logic.
    - **`components`**: Reusable UI elements like `CakeCard` and `CakePopup`.
- **`di`**: Hilt modules (`NetworkModule`, `RepositoryModule`) for providing singleton dependencies.
- **`ui/theme`**: Centralized location for colors, typography, and shapes.
- **`util`**: Utility classes and constants like `CakeConstants`.

## 🚦 Getting Started

### Prerequisites

- Android Studio Ladybug or newer.
- JDK 17 or higher.
- Android SDK 24+.

### Lint run following commands

- Android Lint: ./gradlew :app:lintDebug
- Detekt: ./gradlew :app:detekt
- Ktlint: ./gradlew :app:ktlintCheck
- Auto-format: ./gradlew :app:ktlintFormat

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/cakes-android.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical device.
