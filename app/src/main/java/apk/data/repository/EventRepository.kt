package apk.data.repository // Menentukan paket tempat file EventRepository berada.

import apk.data.response.DetailEventResponse // Mengimpor kelas DetailEventResponse untuk menguraikan respons detail event.
import apk.data.retrofit.ApiService // Mengimpor ApiService untuk berinteraksi dengan API.
import apk.data.response.ListEventsItem // Mengimpor ListEventsItem untuk merepresentasikan item-event dalam daftar event.

class EventRepository( // Mendeklarasikan kelas EventRepository.
    private val apiService: ApiService // Menerima ApiService sebagai dependensi untuk memanggil API.
) {

    suspend fun getActiveEvents(): List<ListEventsItem> { // Mendeklarasikan metode suspend untuk mendapatkan event aktif.
        return try {
            // Memanggil API secara langsung menggunakan Coroutines
            val response = apiService.getActiveEvents() // Mengambil respons dari API untuk event aktif.
            response.listEvents // Mengembalikan daftar event dari respons.
        } catch (e: Exception) {
            // Tangani jika ada error saat memanggil API
            emptyList() // Mengembalikan daftar kosong pada saat error.
        }
    }

    suspend fun getCompletedEvents(): List<ListEventsItem> { // Mendeklarasikan metode suspend untuk mendapatkan event yang sudah selesai.
        return try {
            // Memanggil API untuk acara selesai
            val response = apiService.getCompletedEvents() // Mengambil respons dari API untuk event selesai.
            response.listEvents // Mengembalikan daftar event dari respons atau daftar kosong jika null.
        } catch (e: Exception) {
            // Tangani jika ada error saat memanggil API
            emptyList() // Mengembalikan daftar kosong pada saat error.
        }
    }

    suspend fun getEventDetail(id: Int): DetailEventResponse? { // Mendeklarasikan metode suspend untuk mendapatkan detail acara berdasarkan ID.
        return try {
            // Memanggil API untuk mendapatkan detail acara
            val response = apiService.getEventDetail(id) // Mengambil respons detail event dari API.

            // Memeriksa apakah respons sukses dan mengembalikan data
            if (response.isSuccessful) { // Memeriksa apakah respons dari API berhasil.
                response.body() // Mengembalikan DetailEventResponse jika sukses.
            } else {
                null // Mengembalikan null jika respons tidak berhasil.
            }
        } catch (e: Exception) {
            null // Mengembalikan null pada saat error.
        }
    }
}
