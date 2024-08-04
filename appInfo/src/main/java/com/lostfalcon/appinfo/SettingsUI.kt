package com.lostfalcon.appinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

object SettingsUI {

    @Composable
    fun AppInfoScreen() {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "App Info", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = { /* Send feedback logic */ }) {
                Text("Send Feedback")
            }

            Button(onClick = { /* Rate app logic */ }) {
                Text("Rate Application")
            }

            Button(onClick = { /* Open source code logic */ }) {
                Text("Source Code")
            }

            // ... (Other Apps section - use LazyColumn/LazyRow if needed)
        }
    }
}