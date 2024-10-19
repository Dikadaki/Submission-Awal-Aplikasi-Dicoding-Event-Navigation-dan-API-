package apk.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import apk.data.repository.EventRepository
import apk.data.retrofit.ApiService
import apk.viewmodel.EventDetailViewModel
import apk.viewmodel.EventDetailViewModelFactory
import com.DhikaWisata.wisatareview.R
import com.bumptech.glide.Glide
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: EventDetailViewModel
    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Contoh: observasi isLoading LiveData dari ViewModel
        viewModel.isloading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        Log.d("EventDetailActivity", "onCreate: Activity dimulai")

        // Mendapatkan ID event dari Intent
        eventId = intent.getStringExtra("EVENT_ID") ?: run {
            Log.e("EventDetailActivity", "Event ID tidak ditemukan di Intent")
            return
        }
        Log.d("EventDetailActivity", "Event ID yang diterima: $eventId")

        // Setup Retrofit dan ApiService
        val retrofit = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev/") // Sesuaikan base URL API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        Log.d("EventDetailActivity", "Retrofit dan ApiService berhasil diinisialisasi")

        // Inisialisasi repository dan ViewModel
        val repository = EventRepository(apiService)
        viewModel = ViewModelProvider(this, EventDetailViewModelFactory(repository))
            .get(EventDetailViewModel::class.java)

        Log.d("EventDetailActivity", "ViewModel berhasil diinisialisasi")

        // Memulai proses mendapatkan detail event dari ViewModel
        viewModel.getEventDetail(eventId.toInt())
        Log.d("EventDetailActivity", "Meminta detail event untuk ID: $eventId")

        // Mengamati LiveData dari ViewModel
        viewModel.detailEvent.observe(this) { detailEvent ->
            Log.d("EventDetailActivity", "Detail event diterima dari ViewModel: $detailEvent")

            detailEvent?.let { response ->
                if (response.error == true) {
                    response.message?.let { Log.e("EventDetailActivity", "Error: $it") }
                    return@let
                }

                val event = response.event
                Log.d("EventDetailActivity", "Detail event tidak null: $event")

                // Inisialisasi UI
                val tvEventName = findViewById<TextView>(R.id.tvEventName)
                val mediaCover = findViewById<ImageView>(R.id.mediaCover)
                val btnOpenLink = findViewById<Button>(R.id.btnOpenLink)
                val tvOwnerName = findViewById<TextView>(R.id.tvOwnerName)
                val tvBeginTime = findViewById<TextView>(R.id.tvBeginTime)
                val tvQuota = findViewById<TextView>(R.id.tvQuota)
                val tvDescription = findViewById<TextView>(R.id.tvDescription)
                val tvSummary = findViewById<TextView>(R.id.tvSummary)
                val tvCityName = findViewById<TextView>(R.id.tvCityName)
                val tvEndTime = findViewById<TextView>(R.id.tvEndTime)
                val tvCategory = findViewById<TextView>(R.id.tvCategory)

                // Set data yang diterima dari ViewModel ke UI
                event?.let {
                    Log.d("EventDetailActivity", "Menampilkan nama event: ${it.name}")
                    tvEventName.text = event.name

                    Log.d("EventDetailActivity", "Menampilkan owner: ${it.ownerName}")
                    tvOwnerName.text = event.ownerName

                    Log.d("EventDetailActivity", "Menampilkan begin time: ${it.beginTime}")
                    tvBeginTime.text = it.beginTime

                    Log.d("EventDetailActivity", "Menampilkan summary: ${it.summary}")
                    tvSummary.text = it.summary

                    Log.d("EventDetailActivity", "Menampilkan city name: ${it.cityName}")
                    tvCityName.text = it.cityName

                    Log.d("EventDetailActivity", "Menampilkan end time: ${it.endTime}")
                    tvEndTime.text = it.endTime

                    Log.d("EventDetailActivity", "Menampilkan category: ${it.category}")
                    tvCategory.text = it.category

                    // Menampilkan informasi kuota dan registrants
                    val quota = it.quota
                    val registrants = it.registrants
                    Log.d("EventDetailActivity", "Menampilkan kuota: $quota, registrants: $registrants")
                    if (quota != null) {
                        tvQuota.text = "$quota kuota tersedia, ${quota - registrants!!} tersisa"
                    }

                    // Menampilkan deskripsi acara dengan HTML
                    Log.d("EventDetailActivity", "Menampilkan deskripsi")
                    tvDescription.text = HtmlCompat.fromHtml(
                        it.description ?: "",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    // Menampilkan gambar media cover menggunakan Glide
                    Log.d("EventDetailActivity", "Menampilkan gambar media cover: ${it.imageLogo}")
                    Glide.with(this)
                        .load(it.imageLogo)
                        .placeholder(R.drawable.placeholder) // Gambar placeholder jika gambar belum tersedia
                        .error(R.drawable.error) // Gambar jika terjadi error saat memuat gambar
                        .into(mediaCover)

                    // Tombol untuk membuka link event
                    btnOpenLink.setOnClickListener {
                        val link = event.link
                        if (!link.isNullOrEmpty()) {
                            Log.d("EventDetailActivity", "Membuka link: $link")
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            startActivity(intent)
                        } else {
                            Log.e("EventDetailActivity", "Link is null or empty")
                        }
                    }
                }
            } ?: run {
                Log.e("EventDetailActivity", "Detail event is null")
            }
        }
    }
}
