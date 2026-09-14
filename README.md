# Dream Cakes 🎂

Dream Cakes is a modern Android application that showcases a delicious variety of cakes. It fetches cake data from a remote API and presents it in a clean, user-friendly interface built with Jetpack Compose and Material 3.

## 🚀 Features

- **Cake Catalog**: Browse a comprehensive list of cakes with their titles and detailed descriptions.
- **Rich Visuals**: High-quality images for each cake, loaded efficiently using Coil 3, with smooth entry animations.
- **Modern UI**: A fully declarative UI built with Jetpack Compose, featuring:
    - **Edge-to-Edge Support**: Optimized for modern Android displays with themed system bars.
    - **Staggered Animations**: Smooth list item entry effects.
    - **Refined Styling**: Custom gradients for improved text legibility over images.
- **Robust State Management**: Seamless handling of Loading, Success, and Error states, including pull-to-refresh and retry functionality.
- **Responsive Layout**: Designed to work gracefully across different screen sizes.

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android's modern toolkit for building native UI.
- **Theming**: [Material 3](https://m3.material.io/) - The latest evolution of Material Design with custom colors, typography, and shapes.
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) - For type-safe REST API consumption.
- **JSON Parsing**: [Gson](https://github.com/google/gson) - For converting JSON to Kotlin objects.
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Built on top of Dagger for simplified DI.
- **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.
- **Architecture**: MVVM (Model-View-ViewModel) with a focus on clean separation of concerns.
- **Concurrency**: Kotlin Coroutines and Flow for asynchronous data handling.

## 🏗 Project Structure

The project follows a modular and layered architecture:

- **`data`**: API service definitions (`CakeApiService`) and data models (`CakeModel`).
- **`repository`**: Abstraction layer for data operations, separating UI from network logic.
- **`presentation`**: UI components, including `screens`, `viewmodel`, and reusable `components`.
- **`di`**: Hilt modules for managing singleton dependencies.
- **`ui/theme`**: Centralized definitions for colors, typography, and shapes.
- **`util`**: Global constants and test helpers.

## 🧪 Testing & Quality

The project maintains high code quality through comprehensive testing and static analysis.

### 📊 Test Coverage
We achieved **89% overall instruction coverage** across the project, including 100% coverage for the repository and data layers.

- **Unit Tests**: Business logic and state transitions in `CakesViewModel`.
- **UI Tests**: Component-level and full-screen interactions using Compose Test rules.
- **E2E Tests**: Verified application launching and main navigation flows with Hilt.

### 🛡 Static Analysis
Integrated linting tools ensure consistent code style and best practices:
- **Android Lint**: Strict checks for Android-specific best practices.
- **Detekt**: Kotlin-specific static analysis for code smells and complexity.
- **Ktlint**: Automated formatting according to the official Kotlin style guide.

## 🚦 Getting Started

### Prerequisites
- Android Studio Ladybug or newer.
- JDK 17 or higher.
- Android SDK 24+.

### Commands

| Task | Command |
| :--- | :--- |
| **Run App** | `./gradlew :app:assembleDebug` |
| **Run Unit Tests** | `./gradlew :app:testDebugUnitTest` |
| **Run UI Tests** | `./gradlew :app:connectedDebugAndroidTest` |
| **Coverage Report** | `./gradlew :app:jacocoTestReport` |
| **Lint Check** | `./gradlew :app:lintDebug` |
| **Detekt Check** | `./gradlew :app:detekt` |
| **Format Code** | `./gradlew :app:ktlintFormat` |

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/cakes-android.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical device.

## 📄 Documentation
The entire codebase is fully documented using **KDoc**. You can hover over any class or function in Android Studio to view its purpose, parameters, and usage details.
