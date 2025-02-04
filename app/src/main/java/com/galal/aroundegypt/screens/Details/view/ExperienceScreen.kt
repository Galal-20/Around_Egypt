package com.galal.aroundegypt.screens.Details.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.aroundegypt.R
import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.model.Details.ExperiencesDetails
import com.galal.aroundegypt.screens.Details.viewModel.ExperienceScreenViewModel

@Composable
fun ExperienceScreen(
    navHostController: NavHostController,
    viewModel: ExperienceScreenViewModel,
    experienceId: String
) {
    LaunchedEffect(experienceId) {
        viewModel.fetchExperienceDetails(experienceId)
    }

    val experienceState by viewModel.experienceDetails.collectAsState()

    when (experienceState) {
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }
        is ApiState.Success -> {
            val experienceDetails = (experienceState as ApiState.Success<ExperiencesDetails>).data
            val experience = experienceDetails.data
            ExperienceDetails(experience)
        }
        is ApiState.Failure -> {
            Text("Error: ${(experienceState as ApiState.Failure).message}")
        }
    }
}

@Composable
fun ExperienceDetails(experience: com.galal.aroundegypt.model.Details.Data) {
    Column(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = rememberAsyncImagePainter(model = experience.cover_photo ?: R.drawable.placeholder),
            contentDescription = "Cover Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )

        Text(text = experience.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = "${experience.city.name}, Egypt", fontSize = 16.sp)
        Text(text = experience.description ?: "No description available", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Icon(painter = painterResource(id = R.drawable.like), contentDescription = "Likes")
            Text(text = "${experience.likes_no} Likes", fontSize = 14.sp)

            Spacer(modifier = Modifier.width(20.dp))

            Icon(painter = painterResource(id = R.drawable.look), contentDescription = "Reviews")
            Text(text = "${experience.views_no} Reviews", fontSize = 14.sp)
        }
    }
}
