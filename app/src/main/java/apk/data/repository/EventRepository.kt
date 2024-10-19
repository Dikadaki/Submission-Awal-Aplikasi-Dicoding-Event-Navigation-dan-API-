package apk.data.repository

import android.util.Log
import apk.data.response.DetailEventResponse
import apk.data.retrofit.ApiService
import apk.data.response.ListEventsItem

class EventRepository(
    private val apiService: ApiService
) {

    companion object {
        private const val TAG = "EventRepository"
    }

    suspend fun getActiveEvents(): List<ListEventsItem> {
        return try {
            Log.d(TAG, "Memulai permintaan untuk mendapatkan event aktif")
            val response = apiService.getActiveEvents()
            Log.d(TAG, "Berhasil mendapatkan event aktif: ${response.listEvents.size} item")
            response.listEvents
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mendapatkan event aktif: ${e.message}")
            emptyList()
        }
    }

    suspend fun getCompletedEvents(): List<ListEventsItem> {
        return try {
            Log.d(TAG, "Memulai permintaan untuk mendapatkan event selesai")
            val response = apiService.getCompletedEvents()
            Log.d(TAG, "Berhasil mendapatkan event selesai: ${response.listEvents.size} item")
            response.listEvents
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mendapatkan event selesai: ${e.message}")
            emptyList()
        }
    }

    suspend fun getEventDetail(id: Int): DetailEventResponse? {
        return try {
            Log.d(TAG, "Memulai permintaan untuk mendapatkan detail event dengan ID: $id")
            val response = apiService.getEventDetail(id)
            if (response.isSuccessful) {
                Log.d(TAG, "Berhasil mendapatkan detail event untuk ID: $id")
                response.body()
            } else {
                Log.e(TAG, "Gagal mendapatkan detail event untuk ID: $id, response code: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mendapatkan detail event untuk ID: $id, error: ${e.message}")
            null
        }
    }
}
