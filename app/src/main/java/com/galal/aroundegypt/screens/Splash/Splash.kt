package com.galal.aroundegypt.screens.Splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.galal.aroundegypt.R
import com.galal.aroundegypt.utils.ReusableLottie
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@Composable
fun SplashScreen(navHostController: NavHostController){
    var showSplashScreen by remember { mutableStateOf(true) }
    val systemUIController = rememberSystemUiController()
    /*SideEffect {
        systemUIController.isSystemBarsVisible = false
        systemUIController.systemBarsDarkContentEnabled = false
    }*/
    LaunchedEffect(Unit) {
        systemUIController.isSystemBarsVisible = false
        systemUIController.setNavigationBarColor(Color.Transparent)
        systemUIController.systemBarsDarkContentEnabled = false

        delay(5000)
        showSplashScreen = false
        withContext(Dispatchers.Main){
            navHostController.navigate("home_screen"){
                popUpTo("splash_screen"){inclusive = true}

            }
        }
        systemUIController.isSystemBarsVisible = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = stringResource(R.string.background),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            ReusableLottie(R.raw.arrow, null, size = 280.dp)

            Text(
                text = stringResource(R.string.around_egypt),
                fontSize = 22.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


