package apk.data.retrofit

import retrofit2.http.GET
import retrofit2.Call
import apk.data.response.EventResponse
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("events")
    fun getActiveEvents(
        @Query("active") active: Int = 1
    ): Call<EventResponse>

    @GET("events")
    fun getCompletedEvents(
        @Query("active") active: Int = 0
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<EventResponse>


}
