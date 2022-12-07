# SoundView
## Project Setup

1. Clone
1. Change Gradle's JDK Settings (if not already set as default): 
`Preferences > Build, Execution, Deployment > BuildTools > Gradle > Gradle JDK > 11`

## Instructions
1. Enter text in the input field to send to the Dall-E API for Image generation
2. Wait around 6-8 seconds for the images to load.
3. Select an image, click download. 
4. The image will be saved in the android/data/at.ac.tuwien.inso/files folder.

## Preamble & Disclaimer

This **Android Application** is provided by Alen Bisanovic and Vlad Popescu-Vifor. The goal of this project is to create an innovative app by applying important concepts of the Android SDK and its related components in combination with several API's. 


## Project Overview & Architecture

The main goal of our app is generating an AI illustration that would match the lyrics of a song [(User Story 1)](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/1).
Our concept of the app would be having the phone listen to a song, obtain its name using ShazamSDK [(User Story 3)](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/3), then use the name to obtain the lyrics of the song using a lyrics API. Afterwards, NLP would be applied on the lyrics for the most important words found in the text. These words would then be entered into the Stable-Diffusion API, which will generate an AI image based on those words. 
Additionally, the user will also be able to view a history of the generated images [(User Story 2)](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/2) and save them to the local storage of the mobile phone [User Story 7](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/7). Another important feature is manually entering a song name [(User Story 5)](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/5) and viewing its lyrics [(User Story 4)](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/4), as well as NLP statistics based on the text [(User Story 8)](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/8). A history of the previous scanned songs would also be available for the user to re-select [(User Story 6)](https://student.inso.tuwien.ac.at/mobile-app-software-engineering/ws22/track-a-team-09-android/-/issues/6). 
![MSE](uploads/4a3af3bc7beeb1738f0ba9bbe45e97f4/MSE.png)


### Architecture: 

* [Android Studio](https://developer.android.com/studio/)
* [Android Resources and Localization](https://developer.android.com/guide/topics/resources/providing-resources)
* [Gradle Build Tools](https://gradle.org/) and [Dependency handling](https://developer.android.com/studio/build/dependencies)
* [MVVM Architecture](https://developer.android.com/jetpack/guide) and [Repository Pattern](https://developer.android.com/jetpack/guide#overview)
* [Room Database](https://developer.android.com/training/data-storage/room)
* Dependency Injection with [Koin](https://insert-koin.io/)
* [Structured Concurrency with Kotlin Coroutines](https://developer.android.com/kotlin/coroutines)
* [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel), Android Lifecycle and [Reactive Programming](https://en.wikipedia.org/wiki/Reactive_programming).

### UI
* [Jetpack Compose](https://developer.android.com/jetpack/compose/documentation)
* User Interface Design
* Resources (Strings, Images, Icons)

### Dataflow within the application - coming soon
<br />

### Screenshots !


## Authors in Alphabetic Order

* Alen Bisanovic 
* Vlad Popescu-Vifor

<br />

<img src="doc/logos.png" width="30%" height="30%" />

<br />
<br />
