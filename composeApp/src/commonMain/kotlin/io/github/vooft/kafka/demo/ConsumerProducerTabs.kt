package io.github.vooft.kafka.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.vooft.kafka.cluster.KafkaCluster
import io.github.vooft.kafka.common.types.KafkaTopic
import io.github.vooft.kafka.demo.KafkaTab.CONSUMER_TAB
import io.github.vooft.kafka.demo.KafkaTab.PRODUCER_TAB
import io.github.vooft.kafka.demo.consumer.ConsumerScreen
import io.github.vooft.kafka.demo.producer.ProducerScreen
import io.github.vooft.kafka.producer.KafkaTopicProducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.io.readString

@Composable
fun ConsumerProducerTabs(kafkaCluster: KafkaCluster, topic: KafkaTopic, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var producer by remember { mutableStateOf<KafkaTopicProducer?>(null) }
    var consumedMessage by remember { mutableStateOf(mutableListOf<String>()) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) { producer = kafkaCluster.createProducer(topic) }
        scope.launch(Dispatchers.IO) {
            val consumer = kafkaCluster.createConsumer(topic)
            while (true) {
                val records = consumer.consume()
                println("received ${records.size} records")
                records.forEach {
                    val key = it.key.readString()
                    val value = it.value.readString()

                    consumedMessage += value

                    println("Received message: $key : $value")
                }

                delay(1000)
            }
        }
    }

    var selectedTab by remember { mutableStateOf(PRODUCER_TAB) }
    Column(modifier = modifier.fillMaxWidth()) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTab.ordinal,
            tabs = {
                Tab(selected = selectedTab == PRODUCER_TAB, onClick = { selectedTab = PRODUCER_TAB }) {
                    Text("Producer")
                }
                Tab(selected = selectedTab == CONSUMER_TAB, onClick = { selectedTab = CONSUMER_TAB }) {
                    Text("Consumer")
                }
            }
        )

        when (selectedTab) {
            PRODUCER_TAB -> ProducerScreen(producer, modifier = Modifier.fillMaxWidth())
            CONSUMER_TAB -> ConsumerScreen(consumedMessage, modifier = Modifier.fillMaxWidth())
        }
    }
}

enum class KafkaTab {
    PRODUCER_TAB,
    CONSUMER_TAB
}
