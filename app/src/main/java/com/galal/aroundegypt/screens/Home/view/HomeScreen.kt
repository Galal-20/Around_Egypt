package com.galal.aroundegypt.screens.Home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.galal.aroundegypt.utils.NoInternetConnection
import com.galal.aroundegypt.utils.networkListener

@Composable
fun HomeScreen(navHostController: NavHostController) {

    val isNetworkAvailable = networkListener()
    if (!isNetworkAvailable.value){
        NoInternetConnection()
    }else{
        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.White)
        ) {
            Text(text = "Home Screen", color = Color.Black, textAlign = TextAlign.Center)
        }
    }
}