package com.galal.aroundegypt.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.galal.aroundegypt.R

@Composable
fun ReusableLottie(
    @RawRes lottieRes: Int,
    backgroundImageRes: Int?,
    size: Dp? = null,
    speed: Float = 1f,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = speed,
    )

    Box(
        modifier = Modifier
            .size(size!!)
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent)

    ) {
        if (backgroundImageRes != null) {
            Image(
                painter = painterResource(backgroundImageRes!!),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.fillMaxSize()

        )
    }
}


@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .testTag("CircularProgressIndicator")
        ,
        contentAlignment = Alignment.Center,

        ) {
        CircularProgressIndicator(color = Color.Black)
    }
}


@Composable
fun NoInternetConnection() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReusableLottie(
                R.raw.no_internet,
                null,
                size = 400.dp, speed = 1f)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(R.string.no_internet_connection),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}



@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}


@Composable
fun WelcomeSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(
            text = stringResource(R.string.welcome),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = stringResource(R.string.welcome_description),
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
