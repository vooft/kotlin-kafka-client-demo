package io.github.vooft.kafka.demo.connect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import io.github.vooft.kafka.common.types.BrokerAddress
import io.github.vooft.kafka.common.types.KafkaTopic
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ConnectScreen(
    onSuccess: (BrokerAddress, KafkaTopic) -> Unit,
    onCancel: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    var host by remember { mutableStateOf(defaultKafkaHost()) }
    var port by remember { mutableStateOf(defaultKafkaPort().toString()) }
    var topic by remember { mutableStateOf("my-test-topic") }

    Column(modifier = modifier.fillMaxWidth()) {
        Text("Please connect to Kafka broker:")

        Spacer(modifier = Modifier.fillMaxWidth())

        TextField(
            value = host,
            onValueChange = { host = it },
            label = { Text("Host") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = port,
            onValueChange = { port = it },
            label = { Text("Port") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Topic") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { onSuccess(BrokerAddress(host, port.toInt()), KafkaTopic(topic)) }) {
                Text("Connect")
            }

            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}

