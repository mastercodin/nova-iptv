package com.open.iptv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.open.iptv.ui.MainScreen
import com.open.iptv.ui.theme.OpenIptvTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            OpenIptvTheme {
                MainScreen()
            }
        }
    }
}
