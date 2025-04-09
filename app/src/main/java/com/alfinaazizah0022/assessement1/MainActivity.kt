package com.alfinaazizah0022.assessement1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alfinaazizah0022.assessement1.navigation.SetupNavGraph
import com.alfinaazizah0022.assessement1.ui.theme.Assessement1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assessement1Theme {
                SetupNavGraph()
            }
        }
    }
}

