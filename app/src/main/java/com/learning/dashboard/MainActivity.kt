package com.learning.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.learning.dashboard.presentation.navigation.AppNavigation
import com.learning.dashboard.ui.theme.LearningDashboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningDashboardTheme {
                AppNavigation()
            }
        }
    }
}
