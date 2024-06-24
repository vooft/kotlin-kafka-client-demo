import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vooft.kafka.demo.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "kafka-demo",
    ) {
        App()
    }
}
