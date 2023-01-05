package at.ac.tuwien.inso.ui.navigation

/**
 * This defines the possible routes used in the [SoundViewNavigation].
 */
enum class SoundViewScreens(val route: String) {
    ImageChooserScreen("imageChoose"),
    ImageToStorageScreen("imageStorage"),
    ImageGenerateDevToolScreen("imageDev"),
    SoundRecorderScreen("soundRec")
}

