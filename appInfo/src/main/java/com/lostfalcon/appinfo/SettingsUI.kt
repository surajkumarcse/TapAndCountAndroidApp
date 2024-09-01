package com.lostfalcon.appinfo

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

object SettingsUI {
    const val LOG_TAG = "SettingsUI"

    @Composable
    fun AppInfoScreen(navController: NavController) {
        BackHandler {
            Log.e(LOG_TAG, "back invoked")
            navController.popBackStack()
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.primary
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val context = LocalContext.current

            Spacer(modifier = Modifier.size(48.dp))

            Text(text = stringResource(id = R.string.app_info),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(text = stringResource(id = R.string.my_info),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(32.dp),
                color = MaterialTheme.colorScheme.tertiary
            )

            Button(onClick = { SettingsUIUtil.openGmail(context = context) }) {
                Text(
                    stringResource(R.string.send_feedback),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Button(onClick = { SettingsUIUtil.openGooglePlayStore(context = context) }) {
                Text(
                    stringResource(id = R.string.rate_application),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Button(onClick = { SettingsUIUtil.openGitHub(context) }) {
                Text(
                    stringResource(id = R.string.source_code),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

        }
    }
}