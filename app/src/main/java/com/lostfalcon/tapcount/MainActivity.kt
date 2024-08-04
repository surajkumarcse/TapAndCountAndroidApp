package com.lostfalcon.tapcount

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.lostfalcon.appinfo.SettingsUI
import com.lostfalcon.tapcount.SessionInfo.CentralCountInfo
import com.lostfalcon.tapcount.SessionInfo.HistoryInfoUnit
import com.lostfalcon.tapcount.Util.Constants
import com.lostfalcon.tapcount.ui.theme.TapCountTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels()
    val LOG_TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TapCountTheme {
                TapAndCountHomeScreen(viewModel)
            }
        }

        viewModel.doNotify(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(LOG_TAG, "$intent")
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            when(it.getStringExtra(Constants.NOTIFICATION_TYPE)) {
                Constants.NOTIFICATION_TAP_ACTION -> {
                    viewModel.incrementCount(this)
                }
                Constants.NOTIFICATION_UNDO_ACTION -> {
                    viewModel.decrementCount(this)
                }
                else -> {
                    Log.e(LOG_TAG, "Unknown Notification Type received : ${it.getStringExtra(Constants.NOTIFICATION_TYPE)}")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(LOG_TAG, "requestCode: $requestCode, resultCode: $resultCode, data: $data")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause")
        viewModel.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (viewModel.isBackPressed.value) {
            super.onBackPressed()
            return
        }

        viewModel.isBackPressed.value = true
        Toast.makeText(
            this,
            this.resources.getText(R.string.back_button_first_tap_message),
            Toast.LENGTH_SHORT
        ).show()

        viewModel.viewModelScope.launch {
            delay(2000)
            viewModel.isBackPressed.value = false
        }
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if(event == null) return super.dispatchKeyEvent(event)
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    viewModel.incrementCount(this)
                    return true
                }
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    viewModel.decrementCount(this)
                    return true
                }
            }
            else -> return super.dispatchKeyEvent(event)
        }

        return super.dispatchKeyEvent(event)
    }
}

@Composable
fun TapAndCountHomeScreen(viewModel: MainActivityViewModel) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        MyMenu(viewModel)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.primary
            )
            AnimatedTapBox(viewModel)
            Button(onClick = { viewModel.decrementCount(context) }) {
                Text(
                    text = stringResource(id = R.string.undo_button_text),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    } else {
        MyMenu(viewModel)
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.primary
                )
                AnimatedTapBox(viewModel)
            }
            Button(onClick = { viewModel.decrementCount(context) }) {
                Text(
                    text = stringResource(id = R.string.undo_button_text),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    SettingsUI.AppInfoScreen()
//    TapCountTheme {
//        HistoryItemBox(historyInfoUnit = HistoryInfoUnit(54, "June 21, 2024"))
//    }
}

@Composable
fun AnimatedTapBox(viewModel: MainActivityViewModel) {
    val context = LocalContext.current
    var toggled by remember {
        mutableStateOf(false)
    }

    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

    val animatedPadding by animateDpAsState(
        if (toggled) {
            0.dp
        } else {
            20.dp
        },
        label = "padding"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .padding(animatedPadding)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                toggled = !toggled
                viewModel.incrementCount(context)
            }
    ) {
        LaunchedEffect(toggled) {
            if (toggled) {
                delay(viewModel.ANIMATION_DELAY)
                toggled = !toggled
            }
        }
        Text(
            numberFormat.format(viewModel.count.value),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 100.sp,
            softWrap = true,
        )
    }
}

@Composable
fun HistoryItemBox(historyInfoUnit: HistoryInfoUnit) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

    Box(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = numberFormat.format(historyInfoUnit.value),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = historyInfoUnit.date,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun ItemListDialog(items: List<HistoryInfoUnit>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close") // todo: surakuma take it from outside
            }
        },
        text = {
            LazyColumn {
                items(items.size) { index ->
                    HistoryItemBox(items[index])
                }
            }
        }
    )
}

@Composable
fun MyMenu(viewModel: MainActivityViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showAppInfoDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.TopStart)
        ) {
            IconButton(
                onClick = { showMenu = true },
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = stringResource(R.string.history_button_description),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.reset_button_text),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        showMenu = false
                        viewModel.onResetClicked(
                            context
                        )
                    })
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.history_button_text),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        showMenu = false
                        showHistoryDialog = true
                    })
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.app_info_button),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        showMenu = false
                        showHistoryDialog = false
                        showAppInfoDialog = true
                    })
            }
        }
    }

    if (showHistoryDialog) {
        ItemListDialog(items = viewModel.onHistoryClicked(
            context
        ), onDismiss = { showHistoryDialog = false })
    }

    if (showAppInfoDialog) {
        SettingsUI.AppInfoScreen()
    }
}
