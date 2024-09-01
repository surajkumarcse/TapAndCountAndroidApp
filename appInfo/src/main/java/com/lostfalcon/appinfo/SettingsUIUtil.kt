package com.lostfalcon.appinfo

import android.content.Context
import android.content.Intent
import android.net.Uri

object SettingsUIUtil {

    fun openGooglePlayStore(context: Context) {
        val appPackageName = context.packageName
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser if Google Play is not installed
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
            context.startActivity(intent)
        }
    }

    fun openGmail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("lostfalconofficial@gmail.com")) // Replace with your email
            putExtra(Intent.EXTRA_SUBJECT, "Feedback Tap & Count")
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openGitHub(context: Context) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://github.com/surajkumarcse/TapAndCountAndroidApp")
        )
        context.startActivity(intent)
    }

}