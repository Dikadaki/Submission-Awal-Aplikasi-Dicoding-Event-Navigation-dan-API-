package apk.data.retrofit // Menentukan paket tempat file ApiService berada.

import apk.data.response.DetailEventResponse // Mengimpor kelas DetailEventResponse untuk menguraikan respons detail event.
import apk.data.response.EventResponse // Mengimpor kelas EventResponse untuk menguraikan respons daftar event.
import retrofit2.Response // Mengimpor kelas Response dari Retrofit untuk merepresentasikan respons HTTP.
import retrofit2.http.GET // Mengimpor anotasi GET dari Retrofit untuk permintaan HTTP GET.
import retrofit2.http.Path // Mengimpor anotasi Path untuk menggantikan bagian dari URL.
import retrofit2.http.Query // Mengimpor anotasi Query untuk menambahkan parameter query pada permintaan.

interface ApiService { // Mendeklarasikan interface ApiService untuk mendefinisikan metode API.

    @GET("events") // Menandai metode berikut sebagai permintaan GET untuk endpoint "events".
    suspend fun getActiveEvents( // Mendeklarasikan metode suspend getActiveEvents untuk mendapatkan event aktif.
        @Query("active") active: Int = 1 // Menambahkan parameter query "active" dengan nilai default 1 (untuk event aktif).
    ): EventResponse // Mengembalikan objek EventResponse yang berisi daftar event.

    @GET("events") // Menandai metode berikut sebagai permintaan GET untuk endpoint "events" yang sama.
    suspend fun getCompletedEvents( // Mendeklarasikan metode suspend getCompletedEvents untuk mendapatkan event yang sudah selesai.
        @Query("active") active: Int = 0 // Menambahkan parameter query "active" dengan nilai default 0 (untuk event tidak aktif).
    ): EventResponse // Mengembalikan objek EventResponse yang berisi daftar event.

    @GET("event/{id}") // Menandai metode berikut sebagai permintaan GET untuk endpoint "event/{id}".
    suspend fun getEventDetail( // Mendeklarasikan metode suspend getEventDetail untuk mendapatkan detail dari event berdasarkan ID.
        @Path("id") id: Int // Mengambil ID dari parameter fungsi dan menempatkannya di URL.
    ): Response<DetailEventResponse> // Mengembalikan objek Response yang berisi DetailEventResponse untuk respons detail event.
}
