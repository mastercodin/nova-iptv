package com.open.iptv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.open.iptv.ui.MainScreen
import com.open.iptv.ui.MainViewModel
import com.open.iptv.ui.theme.OpenIptvTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !viewModel.isReady.value }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OpenIptvTheme {
                MainScreen(viewModel)
            }
        }
    }
}
