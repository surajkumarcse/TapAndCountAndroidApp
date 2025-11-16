package com.lostfalcon.tapcount

import android.app.NotificationManager
import android.content.Context
import android.media.ToneGenerator
import android.os.Vibrator
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MainActivityViewModelTest {
    val viewModel = MainActivityViewModel()
    val context = mock<Context>()
    val mockVibrator = mock<Vibrator>()
    val toneGenerator = mock<ToneGenerator>()
    val notificationManager = mock<NotificationManager>()
    val resources = mock<android.content.res.Resources>()

    @Before
    fun setUp() {
        viewModel.count.value = 0
        whenever(context.getSystemService(Vibrator::class.java)).thenReturn(mockVibrator)
        whenever(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager)
        whenever(context.resources).thenReturn(resources)
        whenever(resources.getString(any())).thenReturn("")
        viewModel.toneGenerator = toneGenerator
    }

    @Test
    fun `decrementCount should decrease count by 1`() {
        viewModel.decrementCount(context)
        assert(viewModel.count.value == 0)
    }

    @Test
    fun `onResetClicked should set count to 0`() {
        viewModel.count.value = 0
        viewModel.onResetClicked(context)
        assert(viewModel.count.value == 0)
    }

    @Test
    fun `onPause should release toneGenerator`() {
        viewModel.onPause()
        Mockito.verify(toneGenerator).release()
    }

    @Test
    fun `doVibrate should call vibrate on Vibrator`() {
        viewModel.doVibrate(context, 0)
        Mockito.verify(mockVibrator).vibrate(1000L)
    }

    @Test
    fun `playtone should call startTone on ToneGenerator`() {
        viewModel.playTone()
        Mockito.verify(toneGenerator).startTone(anyInt(), anyInt())
    }
}