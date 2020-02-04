package com.bosqueoeste.dockapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRemoteConfig()
    }

    fun initRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    textHome.text = remoteConfig.getString(TEXT_HOME_KEY)
                    Log.i("Firebase", "Remote config fetch success")

                } else {
                    Log.i("Firebase", "Remote config fetch fail")
                }
            }
    }

    companion object {
        private const val TEXT_HOME_KEY = "textHome"
    }
}
