package com.galal.aroundegypt.screens.Home.viewModel

import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.data.repository.HomeRepositoryImpl
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.Data
import com.galal.aroundegypt.model.Recommanded.GmapLocation
import com.galal.aroundegypt.model.Recommanded.Meta
import com.galal.aroundegypt.model.Recommanded.OpeningHours
import com.galal.aroundegypt.model.Recommanded.Pagination
import com.galal.aroundegypt.model.Recommanded.RecommendedExperiences
import com.galal.aroundegypt.model.Recommanded.Review
import com.galal.aroundegypt.model.Recommanded.Tag
import com.galal.aroundegypt.model.Recommanded.TicketPrice
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test

class HomeViewModelTest{

    private lateinit var viewModel: HomeViewModel
    private val  homeRepo: HomeRepositoryImpl = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(homeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchExperiences should update experiences state when API call is successful`() = runBlocking {
        val mockCity = com.galal.aroundegypt.model.Recommanded.City("Cairo", 1,"a",1)
        val mockGmapLocation = GmapLocation(emptyList(), "longitude")
        val mockOpeningHours = OpeningHours(emptyList(), emptyList(), emptyList(), emptyList(),
            emptyList(), emptyList(), emptyList())
        val mockReviews = listOf(Review("User1", "Great place!", "ex","1","a", 1))
        val mockTags = listOf(Tag("History", 1,"a", 1))
        val mockTicketPrices = listOf(TicketPrice(100, "a"))

        val mockResponse = RecommendedExperiences(
            data = listOf(
                Data(id = "1",
                    title = "Experience 1",
                    address = "123 Street, Cairo",
                    audio_url = "http://example.com/audio.mp3",
                    city = mockCity,
                    cover_photo = "http://example.com/photo.jpg",
                    description = "A great experience.",
                    detailed_description = "Detailed description here.",
                    era = null,
                    experience_tips = emptyList(),
                    famous_figure = "Famous Figure",
                    founded = "1990",
                    gmap_location = mockGmapLocation,
                    has_audio = true,
                    has_video = 1,
                    is_liked = null,
                    likes_no = 100,
                    opening_hours = mockOpeningHours,
                    period = null,
                    rating = 4,
                    recommended = 1,
                    reviews = mockReviews,
                    reviews_no = 10,
                    starting_price = 50,
                    tags = mockTags,
                    ticket_prices = mockTicketPrices,
                    tour_html = "<html>Tour info</html>",
                    //translated_opening_hours = mockTranslatedOpeningHours,
                    views_no = 200
                )
            ),
            meta = Meta(200, emptyList()),
            pagination = Pagination()
        )
        coEvery { homeRepo.fetchRecommendedExperiences() } returns ApiState.Success(mockResponse)

        viewModel.fetchExperiences()

        val result = viewModel.experiences.value
        assert(result is ApiState.Success)
        assertEquals(mockResponse, (result as ApiState.Success).data)
    }

    @Test
    fun `fetchExperiences should update experiences state with failure when API call fails`() = runBlocking {
        val mockErrorMessage = "Network Error"
        coEvery { homeRepo.fetchRecommendedExperiences() } returns ApiState.Failure(mockErrorMessage)

        viewModel.fetchExperiences()

        val result = viewModel.experiences.value
        assert(result is ApiState.Failure)
        assertEquals(mockErrorMessage, (result as ApiState.Failure).message)
    }


    @Test
    fun `fetchMostRecentExperiences should update mostRecentExperiences state when API call is successful`() = runBlocking {
        val mockCity = com.galal.aroundegypt.model.Most.City("Cairo", 1, "a", 1)
        val mockGmapLocation = com.galal.aroundegypt.model.Most.GmapLocation(emptyList(), "longitude")
        val mockOpeningHours = com.galal.aroundegypt.model.Most.OpeningHours(emptyList(), emptyList(), emptyList(), emptyList(),
            emptyList(), emptyList(), emptyList())
        val mockReviews = listOf(com.galal.aroundegypt.model.Most.Review("User1", "Great place!", "ex", "1", "a", 1))
        val mockTags = listOf(com.galal.aroundegypt.model.Most.Tag("History", 1, "a", 1))
        val mockTicketPrices = listOf(com.galal.aroundegypt.model.Most.TicketPrice(100, "a"))

        val mockResponse = MostRecentExperiences(
            data = listOf(
                com.galal.aroundegypt.model.Most.Data(id = "1",
                    title = "Most Recent Experience",
                    address = "123 Street, Cairo",
                    audio_url = "http://example.com/audio.mp3",
                    city = mockCity,
                    cover_photo = "http://example.com/photo.jpg",
                    description = "A great recent experience.",
                    detailed_description = "Detailed recent description here.",
                    era = null,
                    experience_tips = emptyList(),
                    famous_figure = "Famous Figure",
                    founded = "1990",
                    gmap_location = mockGmapLocation,
                    has_audio = true,
                    has_video = 1,
                    is_liked = null,
                    likes_no = 100,
                    opening_hours = mockOpeningHours,
                    period = null,
                    rating = 4,
                    recommended = 1,
                    reviews = mockReviews,
                    reviews_no = 10,
                    starting_price = 50,
                    tags = mockTags,
                    ticket_prices = mockTicketPrices,
                    tour_html = "<html>Tour info</html>",
                    views_no = 200,
                )
            ),
            meta = com.galal.aroundegypt.model.Most.Meta(200, emptyList()),
            pagination = com.galal.aroundegypt.model.Most.Pagination()
        )

        coEvery { homeRepo.fetchMostRecentExperiences() } returns ApiState.Success(mockResponse)

        viewModel.fetchMostRecentExperiences()

        val result = viewModel.mostRecentExperiences.value
        assert(result is ApiState.Success)
        assertEquals(mockResponse, (result as ApiState.Success).data)
    }


    @Test
    fun `fetchMostRecentExperiences should update mostRecentExperiences state with failure when API call fails`() = runBlocking {
        val mockErrorMessage = "Network Error"
        coEvery { homeRepo.fetchMostRecentExperiences() } returns ApiState.Failure(mockErrorMessage)

        viewModel.fetchMostRecentExperiences()

        val result = viewModel.mostRecentExperiences.value
        assert(result is ApiState.Failure)
        assertEquals(mockErrorMessage, (result as ApiState.Failure).message)
    }


}