@file:OptIn(ExperimentalMaterial3Api::class)

package com.galal.aroundegypt.screens.Home.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.galal.aroundegypt.R
import com.galal.aroundegypt.data.api.ApiClient
import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.Data
import com.galal.aroundegypt.screens.Home.viewModel.HomeViewModel
import com.galal.aroundegypt.utils.LoadingIndicator
import com.galal.aroundegypt.utils.NoInternetConnection
import com.galal.aroundegypt.utils.SectionTitle
import com.galal.aroundegypt.utils.WelcomeSection
import com.galal.aroundegypt.utils.networkListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.PaddingValues as PaddingValues1


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel) {
    val isNetworkAvailable = networkListener()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var selectedRecommendedExperience by remember { mutableStateOf<Data?>(null) }
    var selectedMostRecentExperience by remember { mutableStateOf<com.galal.aroundegypt.model.Most.Data?>(null) }

    val searchResults by viewModel.searchResults.collectAsState()
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }




    LaunchedEffect(isNetworkAvailable.value) {
        if (isNetworkAvailable.value) {
            viewModel.fetchExperiences()
            viewModel.fetchMostRecentExperiences()
        }
    }

    LaunchedEffect(searchText) {
        if (searchText.text.isNotEmpty()) {
            viewModel.searchExperiences(searchText.text)
        }
    }

    LaunchedEffect(bottomSheetState.isVisible) {
        if (!bottomSheetState.isVisible) {
            selectedRecommendedExperience = null
            selectedMostRecentExperience = null
        }
    }

    if (!isNetworkAvailable.value) {
        NoInternetConnection()
    }else {

        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            sheetContent = {
                when {
                    selectedRecommendedExperience != null -> {
                        ExperienceDetailsBottomSheet(
                            experience = selectedRecommendedExperience!!,
                            viewModel
                        )
                    }

                    selectedMostRecentExperience != null -> {
                        ExperienceDetailsBottomSheetMost(experience = selectedMostRecentExperience!!)
                    }

                    else -> {
                        Box(modifier = Modifier.height(1.dp))
                    }
                }
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                TopBar(
                    viewModel,
                    isSearching = isSearching,
                    searchText = searchText,
                    onSearchChanged = { isSearching = it },
                    onSearchTextChanged = { searchText = it }
                )

                if (isSearching) {
                    if (searchResults is ApiState.Success) {
                        val successResult = searchResults as ApiState.Success
                        val experiences = successResult.data.data
                        if (experiences.isNotEmpty()) {
                            SectionTitle("Search Results")
                            LazyColumn(
                                contentPadding = PaddingValues1(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(experiences) { experience ->
                                    MostRecentCard(experience, viewModel) {
                                        selectedMostRecentExperience = it
                                        coroutineScope.launch {
                                            bottomSheetState.show()
                                        }
                                    }
                                }
                            }
                        } else {
                            Text("No results found", modifier = Modifier.padding(16.dp))
                        }
                    } else if (searchResults is ApiState.Loading) {
                        LoadingIndicator()
                    } else if (searchResults is ApiState.Failure) {
                        Text("Error occurred while searching", modifier = Modifier.padding(16.dp))
                    }
                } else {
                    WelcomeSection()
                    SectionTitle(title = "Recommended Experiences")
                    HorizontalCardList(viewModel = viewModel) { experience ->
                        selectedRecommendedExperience = experience
                        coroutineScope.launch { bottomSheetState.show() }
                    }
                    SectionTitle("Most Recent")
                    MostRecentList(viewModel) { experience ->
                        selectedMostRecentExperience = experience
                        coroutineScope.launch { bottomSheetState.show() }
                    }
                }
            }
        }
    }

}


@Composable
fun TopBar(
    viewModel: HomeViewModel,
           isSearching: Boolean,
           searchText: TextFieldValue,
           onSearchChanged: (Boolean) -> Unit,
           onSearchTextChanged: (TextFieldValue) -> Unit
) {
    //var searchText  by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (!isSearching) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onSearchChanged(true)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Try \"Luxor\"",
                        style = MaterialTheme.typography.body1.copy(color = Color.Gray)
                    )
                }
            }
        } else {
            OutlinedTextField(
                value = searchText,
                onValueChange = { value ->
                    onSearchTextChanged(value)
                    if (searchText.text.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch{
                            viewModel.searchExperiences(value.text)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                label = { Text("Try \"Luxor\"",color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchText.text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                onSearchTextChanged(TextFieldValue(""))
                            }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Search",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                onSearchChanged(false)
                                keyboardController?.hide()
                            }
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    textColor = Color.Black,
                    cursorColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Filter Icon
        Image(
            painter = painterResource(R.drawable.filter),
            contentDescription = "Filter",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(24.dp)
        )
    }

}


// Recommended Experiences
@Composable
fun HorizontalCardList(viewModel: HomeViewModel, onItemClick: (Data) -> Unit) {
    val experiencesState by viewModel.experiences.collectAsState()

    when (experiencesState) {
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }
        is ApiState.Success -> {
            val experiences = (experiencesState as ApiState.Success<com.galal.aroundegypt.model.Recommanded.RecommendedExperiences>).data.data
            LazyRow(
                contentPadding = PaddingValues1(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(experiences, key = { it.id }) { experience ->
                    RecommendedExperiencesCard(experiences = experience,viewModel, onClick = onItemClick)
                }
            }
        }
        is ApiState.Failure -> {
            Text(text = "Error: ${(experiencesState as ApiState.Failure).message}")
        }
    }
}

@Composable
fun RecommendedExperiencesCard(experiences: Data, viewModel: HomeViewModel,onClick: (Data) -> Unit) {

    var likesCount by remember { mutableIntStateOf(experiences.likes_no ?: 0) }
    var isLiked :Boolean by remember { mutableStateOf((experiences.is_liked ?: false) as Boolean) }


    Column(
        modifier = Modifier.clickable { onClick(experiences) }
    ) {
        Card(
            modifier = Modifier
                .width(370.dp)
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp

        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = experiences.cover_photo ?: R.drawable.placeholder
                ),
                contentDescription = "Cover Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Log.d("RecommendedExperiences", "Experiences data: $experiences")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {

                // "RECOMMENDED" Tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            Color(0x99000000),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Background Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "RECOMMENDED",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Image(painter = painterResource(id = R.drawable.earth),
                    contentDescription = "360 degree",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp).align(Alignment.Center))
                // 360 Icon


                // Info Icon (Top-Right)
                Image(painter = painterResource(id = R.drawable.info),
                    contentDescription = "Info Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp).align(Alignment.TopEnd).padding(8.dp))

                Image(painter = painterResource(id = R.drawable.pictures),
                    contentDescription = "Info Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp).align(Alignment.BottomEnd).padding(8.dp))
                // Gallery Icon (Bottom-Right)

                // Views Count (Bottom-Left)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.look),
                        contentDescription = "views Icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${experiences.views_no}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        //NameRecommended
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.width(370.dp)
                .padding(horizontal = 18.dp, vertical = 15.dp),
        ){
            Column{
                Text(text = "${experiences.title}",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column{
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "${likesCount}",
                        color = Color.Black,
                        fontSize = 14.sp)

                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = if (isLiked) R.drawable.like else R.drawable.outline_like),
                        contentDescription = "Like Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                if (!isLiked) {
                                    viewModel.likeExperience(experiences.id)
                                    likesCount++
                                    isLiked = true
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun ExperienceDetailsBottomSheet(experience: Data, viewModel: HomeViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = experience.cover_photo ?: R.drawable.placeholder),
            contentDescription = "Cover Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(285.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        //title, likes, views
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 18.dp),
            ){
                Column {
                    Text(
                        text = experience.title ?: "Unknown City",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Column {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "Share",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Image(
                            painter = painterResource(id = R.drawable.outline_like),
                            contentDescription = "Like",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "${experience.likes_no ?: 0}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                }
            }
            //City
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 5.dp),) {
                Text(
                    text = "${experience.city?.name ?: 0}, Egypt.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(title = "Description")

        Text(
            text = "${experience.description}",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

    }
}


//___________________________________________________________________________________________________
// Most Recent Experiences
@Composable
fun MostRecentList(viewModel: HomeViewModel, onItemClick: (com.galal.aroundegypt.model.Most.Data) -> Unit) {
    val mostRecentState by viewModel.mostRecentExperiences.collectAsState()

    when(mostRecentState){
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }
        is ApiState.Success -> {
            val mostRecent = (mostRecentState as ApiState.Success<com.galal.aroundegypt.model.Most.MostRecentExperiences>).data.data
            LazyColumn(
                contentPadding = PaddingValues1(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(mostRecent, key = { it.id }) { mostRecent ->
                    MostRecentCard(mostRecent,viewModel, onItemClick = onItemClick)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        is ApiState.Failure -> {
            Text(text = "Error: ${(mostRecentState as ApiState.Failure).message}")
        }
    }
}

@Composable
fun MostRecentCard(mostExperience: com.galal.aroundegypt.model.Most.Data,viewModel: HomeViewModel, onItemClick: (com
    .galal.aroundegypt.model.Most.Data) -> Unit ){
    var likesCount by remember { mutableStateOf(mostExperience.likes_no ?: 0) }
    //var isLiked :Boolean by remember { mutableStateOf((mostExperience.is_liked ?: false) as Boolean) }
    var isLiked: Boolean by remember {
        mutableStateOf(viewModel.likedExperiencesMost.contains(mostExperience.id))
    }

    Column{
        Card(
            modifier = Modifier
                .width(370.dp)
                .height(200.dp)
                .clickable { onItemClick(mostExperience) },
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp

        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = mostExperience.cover_photo ?: R.drawable.placeholder
                ),
                contentDescription = "Cover Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Log.d("MostExperiences", "Experiences data: $mostExperience")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {


                Image(painter = painterResource(id = R.drawable.earth),
                    contentDescription = "360 degree",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp).align(Alignment.Center))


                Image(painter = painterResource(id = R.drawable.info),
                    contentDescription = "Info Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp).align(Alignment.TopEnd).padding(8.dp))

                Image(painter = painterResource(id = R.drawable.pictures),
                    contentDescription = "Info Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp).align(Alignment.BottomEnd).padding(8.dp))

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.look),
                        contentDescription = "views Icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${mostExperience.views_no}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        //NameMostRecent
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier.weight(1f)
            ){
                Text(text = "${mostExperience.title}",
                    color = Color.Black,
                    fontSize = 14.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${likesCount ?: 0}")
                    Spacer(modifier = Modifier.width(5.dp))

                    Image(
                        painter = painterResource(id = if (isLiked) R.drawable.like else R.drawable.outline_like),
                        contentDescription = "Like Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                if (!isLiked) {
                                    isLiked = true
                                    likesCount++
                                    viewModel.likeExperience(mostExperience.id)
                                    viewModel.likedExperiencesMost.add(mostExperience.id)
                                }
                            }
                    )
                }
            }
        }
    }

}


@Composable
fun ExperienceDetailsBottomSheetMost(experience: com.galal.aroundegypt.model.Most.Data) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = experience.cover_photo ?: R.drawable.placeholder),
            contentDescription = "Cover Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(285.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        //title, likes, views
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 18.dp),
        ){
            Column {
                Text(
                    text = experience.title ?: "Unknown City",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Column {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "Share",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painter = painterResource(id = R.drawable.outline_like),
                        contentDescription = "Like",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "${experience.likes_no ?: 0}",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

            }
        }
        //City
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 5.dp),) {
            Text(
                text = "${experience.city.name ?: 0}, Egypt.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(title = "Description")

        Text(
            text = "${experience.description}",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

    }
}






