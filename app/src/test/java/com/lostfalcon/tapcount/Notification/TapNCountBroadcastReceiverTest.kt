package com.lostfalcon.tapcount.Notification

import android.content.Context
import android.content.Intent
import com.lostfalcon.tapcount.SessionInfo.CentralCountInfo
import com.lostfalcon.tapcount.Util.Constants.NOTIFICATION_TAP_ACTION
import com.lostfalcon.tapcount.Util.Constants.NOTIFICATION_UNDO_ACTION
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever


class TapNCountBroadcastReceiverTest {
    private lateinit var tapNCountBroadcastReceiver: TapNCountBroadcastReceiver

    private val context = mock<Context>()
    private val intent = mock<Intent>()

    @Before
    fun setUp() {
        tapNCountBroadcastReceiver = TapNCountBroadcastReceiver()
        CentralCountInfo.count.value = 0
    }

    @Test
    fun onReceive_should_increment_value_when_NOTIFICATION_UNDO_ACTION() {
        whenever(intent.action)
            .thenReturn(NOTIFICATION_UNDO_ACTION)
        tapNCountBroadcastReceiver.onReceive(context, intent)
        assert(CentralCountInfo.count.value == -1)
    }

    @Test
    fun onReceive_should_decrement_value_when_NOTIFICATION_TAP_ACTION() {
        whenever(intent.action)
            .thenReturn(NOTIFICATION_TAP_ACTION)
        tapNCountBroadcastReceiver.onReceive(context, intent)
        assert(CentralCountInfo.count.value == 1)
    }
}