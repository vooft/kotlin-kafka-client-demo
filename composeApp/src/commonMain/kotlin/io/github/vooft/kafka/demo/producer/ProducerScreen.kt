package io.github.vooft.kafka.demo.producer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.vooft.kafka.producer.KafkaTopicProducer
import io.github.vooft.kafka.producer.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun ProducerScreen(producer: KafkaTopicProducer?, modifier: Modifier = Modifier) {
    if (producer == null) {
        Text("Producer is initializing")
        return
    }

    val scope = rememberCoroutineScope()

    var message by remember { mutableStateOf("") }
    var key by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = key,
            onValueChange = { key = it },
            label = { Text("Key") }
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = message,
            onValueChange = { message = it },
            label = { Text("Message") }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch(Dispatchers.IO) {
                    println("sending message: $key : $message")
                    try {
                        val result = producer.send(key, message)
                        println(result)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    key = ""
                    message = ""
                }
            }
        ) {
            Text("Send")
        }
    }
}
