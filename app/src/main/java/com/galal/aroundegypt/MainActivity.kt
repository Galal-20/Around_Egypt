package com.galal.aroundegypt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.galal.aroundegypt.data.api.ApiClient
import com.galal.aroundegypt.data.repository.HomeRepositoryImpl
import com.galal.aroundegypt.screens.Home.view.HomeScreen
import com.galal.aroundegypt.screens.Home.viewModel.HomeViewModel
import com.galal.aroundegypt.screens.Home.viewModel.HomeViewModelFactory
import com.galal.aroundegypt.screens.Splash.SplashScreen
import com.galal.aroundegypt.ui.theme.AroundEgyptTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: HomeViewModel by viewModels {
            HomeViewModelFactory(HomeRepositoryImpl(ApiClient.aroundApi))
        }
        setContent {
            AroundEgyptTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    Scaffold { paddingValues ->
                        NavHost(
                            navController = navController, startDestination = "splash_screen",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("splash_screen") {
                                SplashScreen(navController)
                            }
                            composable("home_screen") {
                                HomeScreen(navHostController = navController, viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

