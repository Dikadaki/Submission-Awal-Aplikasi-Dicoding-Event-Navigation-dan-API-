package apk.data.retrofit // Menentukan paket tempat file ApiConfig berada.

import okhttp3.OkHttpClient // Mengimpor OkHttpClient untuk melakukan permintaan HTTP.
import okhttp3.logging.HttpLoggingInterceptor // Mengimpor interceptor untuk logging permintaan HTTP.
import retrofit2.Retrofit // Mengimpor Retrofit untuk mengelola panggilan API.
import retrofit2.converter.gson.GsonConverterFactory // Mengimpor GsonConverterFactory untuk mengkonversi JSON menjadi objek Kotlin.


class ApiConfig { // Mendeklarasikan kelas ApiConfig.
    companion object { // Mendeklarasikan objek companion untuk akses statis.
        fun getApiService(): ApiService { // Mendeklarasikan metode untuk mendapatkan instansi ApiService.
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) // Membuat interceptor untuk log body dari permintaan dan respons.
            val client = OkHttpClient.Builder() // Membuat builder untuk OkHttpClient.
                .addInterceptor(loggingInterceptor) // Menambahkan interceptor untuk logging ke klien HTTP.
                .build() // Membangun instansi OkHttpClient.

            val retrofit = Retrofit.Builder() // Membuat builder untuk Retrofit.
                .baseUrl("https://event-api.dicoding.dev/") // Menetapkan URL dasar untuk API.
                .addConverterFactory(GsonConverterFactory.create()) // Menambahkan converter untuk mengkonversi JSON ke objek Kotlin menggunakan Gson.
                .client(client) // Menggunakan OkHttpClient yang telah dibuat.
                .build() // Membangun instansi Retrofit.

            return retrofit.create(ApiService::class.java) // Mengembalikan instansi ApiService yang dibuat dari Retrofit.
        }
    }
}
