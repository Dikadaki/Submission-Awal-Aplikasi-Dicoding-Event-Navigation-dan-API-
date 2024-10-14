package apk.data.retrofit

import apk.data.response.DetailEventResponse
import apk.data.response.EventResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getActiveEvents(
        @Query("active") active: Int = 1
    ): EventResponse

    @GET("events")
    suspend fun getCompletedEvents(
        @Query("active") active: Int = 0
    ): EventResponse

    @GET("event/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): Response<DetailEventResponse>

}
