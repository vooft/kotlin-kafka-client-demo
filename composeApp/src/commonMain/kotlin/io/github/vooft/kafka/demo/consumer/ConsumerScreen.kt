package io.github.vooft.kafka.demo.consumer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ConsumerScreen(messages: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Received messages: ${messages.size}", fontSize = 20.sp)
        Spacer(modifier = Modifier.fillMaxWidth())

        messages.forEach {
            Text(it)
        }
    }
}
