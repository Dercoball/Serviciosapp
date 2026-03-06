package com.example.serviciosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.serviciosapp.AppContainer
import com.example.serviciosapp.ui.navigation.ServiciosNavHost
import com.example.serviciosapp.ui.theme.ServiciosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = AppContainer(applicationContext)
        setContent {
            ServiciosAppTheme {
                ServiciosNavHost(appContainer = appContainer)
            }
        }
    }
}
