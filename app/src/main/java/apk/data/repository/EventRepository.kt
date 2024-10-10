package apk.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apk.data.retrofit.ApiService
import apk.data.response.EventResponse
import apk.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventRepository(private val apiService: ApiService) {

    fun getEvents(): LiveData<List<ListEventsItem>> {
        val eventsLiveData = MutableLiveData<List<ListEventsItem>>()

        // Memanggil API untuk mendapatkan daftar acara
        apiService.getActiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    eventsLiveData.value = response.body()!!.listEvents
                } else {
                    // Tangani jika respons tidak sukses
                    eventsLiveData.value = emptyList() // Mengatur daftar kosong
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                // Tangani error jaringan di sini
                eventsLiveData.value = emptyList() // Mengatur daftar kosong
            }
        })
        return eventsLiveData
    }
    fun getCompletedEvents(): LiveData<List<ListEventsItem>> {
        val eventsLiveData = MutableLiveData<List<ListEventsItem>>()

        // Memanggil API untuk mendapatkan daftar acara selesai
        apiService.getCompletedEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    eventsLiveData.value = response.body()!!.listEvents
                } else {
                    // Tangani jika respons tidak sukses
                    eventsLiveData.value = emptyList() // Mengatur daftar kosong
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                // Tangani error jaringan di sini
                eventsLiveData.value = emptyList() // Mengatur daftar kosong
            }
        })
        return eventsLiveData
    }
}


