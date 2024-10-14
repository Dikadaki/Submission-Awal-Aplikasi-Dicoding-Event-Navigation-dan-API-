package apk.data.repository

import apk.data.response.DetailEventResponse
import apk.data.retrofit.ApiService
import apk.data.response.ListEventsItem

class EventRepository(
    private val apiService: ApiService
) {

    suspend fun getActiveEvents(): List<ListEventsItem> {
        return try {
            // Memanggil API secara langsung menggunakan Coroutines
            val response = apiService.getActiveEvents()
            response.listEvents
        } catch (e: Exception) {
            // Tangani jika ada error saat memanggil API
            emptyList() // Mengembalikan daftar kosong pada saat error
        }
    }

    suspend fun getCompletedEvents(): List<ListEventsItem> {
        return try {
            // Memanggil API untuk acara selesai
            val response = apiService.getCompletedEvents()
            response.listEvents // Mengembalikan daftar events atau daftar kosong jika null
        } catch (e: Exception) {
            // Tangani jika ada error saat memanggil API
            emptyList() // Mengembalikan daftar kosong pada saat error
        }
    }

    suspend fun getEventDetail(id: Int): DetailEventResponse? {
        return try {
            // Memanggil API untuk mendapatkan detail acara
            val response = apiService.getEventDetail(id)

            // Memeriksa apakah respons sukses dan mengembalikan data
            if (response.isSuccessful) {
                response.body() // Mengembalikan DetailEventResponse
            } else {
                null // Mengembalikan null jika respons tidak berhasil
            }
        } catch (e: Exception) {
            null // Mengembalikan null pada saat error
        }
    }
}
