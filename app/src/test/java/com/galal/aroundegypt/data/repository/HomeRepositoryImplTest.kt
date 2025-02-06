package com.galal.aroundegypt.data.repository

import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.data.api.AroundApi
import com.galal.aroundegypt.data.local.ExperienceDatabase
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
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeRepositoryImplTest{
    private lateinit var apiService: AroundApi
    private lateinit var database: ExperienceDatabase
    private lateinit var homeRepositoryImpl: HomeRepositoryImpl

    @BeforeEach
    fun setUp() {
        apiService = mockk()
        database = mockk()
        homeRepositoryImpl = HomeRepositoryImpl(apiService, database)
    }

    @Test
    fun `fetchRecommendedExperiences should return success when API call is successful`() = runBlocking {
        val mockCity = com.galal.aroundegypt.model.Recommanded.City("Cairo", 1,"a",1)
        val mockGmapLocation = GmapLocation(emptyList(), "longitude")
        val mockOpeningHours = OpeningHours(emptyList(), emptyList(), emptyList(), emptyList(),
            emptyList(), emptyList(), emptyList())
        val mockReviews = listOf(Review("User1", "Great place!", "ex","1","a", 1))
        val mockTags = listOf(Tag("History", 1,"a", 1))
        val mockTicketPrices = listOf(TicketPrice(100, "a"))

        val mockData = Data(
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
            id = "1",
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
            title = "Historical Tour",
            tour_html = "<html>Tour info</html>",
            //translated_opening_hours = mockTranslatedOpeningHours,
            views_no = 200
        )

        val mockMeta = Meta(200, emptyList())
        val mockPagination = Pagination()
        val mockResponse = RecommendedExperiences(
            data = listOf(mockData),
            meta = mockMeta,
            pagination = mockPagination
        )

        coEvery { apiService.getRecommendedExperiences() } returns mockResponse

        val result = homeRepositoryImpl.fetchRecommendedExperiences()

        assert(result is ApiState.Success)
        TestCase.assertEquals(mockResponse, (result as ApiState.Success).data)
    }

    @Test
    fun `fetchRecommendedExperiences should return failure when API call fails`() = runBlocking {
        coEvery { apiService.getRecommendedExperiences() } throws Exception("Network Error")

        val result = homeRepositoryImpl.fetchRecommendedExperiences()

        assert(result is ApiState.Failure)
        TestCase.assertEquals("Network Error", (result as ApiState.Failure).message)
    }
}