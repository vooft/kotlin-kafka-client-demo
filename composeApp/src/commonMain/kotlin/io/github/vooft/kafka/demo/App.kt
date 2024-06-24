package io.github.vooft.kafka.demo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.vooft.kafka.cluster.KafkaCluster
import io.github.vooft.kafka.common.types.BrokerAddress
import io.github.vooft.kafka.common.types.KafkaTopic
import io.github.vooft.kafka.demo.connect.ConnectScreen
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var brokerAddress by remember { mutableStateOf<BrokerAddress?>(null) }
        var kafkaCluster by remember { mutableStateOf<KafkaCluster?>(null) }
        var kafkaTopic by remember { mutableStateOf<KafkaTopic?>(null) }

        val scope = rememberCoroutineScope()

        DisposableEffect(Unit) {
            onDispose {
                scope.launch {
                    withContext(NonCancellable) {
                        kafkaCluster?.close()
                    }
                }
            }
        }

        Scaffold(modifier = Modifier.fillMaxWidth(),
            topBar = {
                TopAppBar(title = {
                    brokerAddress?.let {
                        Text("Kafka cluster at ${it.hostname}:${it.port}")
                    } ?: Text("Not connected to Kafka cluster")
                })
            }) { innerPadding ->

            val actualCluster = kafkaCluster
            val actualTopic = kafkaTopic

            if (actualCluster != null && actualTopic != null) {
                ConsumerProducerTabs(
                    kafkaCluster = actualCluster,
                    topic = actualTopic,
                    modifier = Modifier.padding(innerPadding)
                )
            } else {
                ConnectScreen(
                    onSuccess = { bootstrap, topic ->
                        brokerAddress = bootstrap
                        kafkaCluster = KafkaCluster(listOf(bootstrap))
                        kafkaTopic = topic
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
