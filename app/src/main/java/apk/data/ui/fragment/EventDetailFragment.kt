package apk.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import apk.data.repository.EventRepository
import apk.data.retrofit.ApiService
import apk.viewmodel.EventDetailViewModel
import apk.viewmodel.EventDetailViewModelFactory
import com.DhikaWisata.wisatareview.R
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventDetailFragment : Fragment() {

    private lateinit var viewModel: EventDetailViewModel
    private lateinit var eventId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Mendapatkan ID event dari argumen fragment
        eventId = arguments?.getString("EVENT_ID") ?: run {
            Log.e("EventDetailFragment", "Event ID tidak ditemukan di argumen")
            return null
        }

        Log.d("EventDetailFragment", "Event ID yang diterima: $eventId")

        // Mengatur layout fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Untuk mencatat seluruh body permintaan dan respons
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        Log.d("EventDetailFragment", "onViewCreated: Memulai inisialisasi Fragment")

        // Setup Retrofit dan ApiService
        val retrofit = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        Log.d("EventDetailFragment", "Retrofit dan ApiService berhasil diinisialisasi")


        // Inisialisasi repository dan ViewModel
        val repository = EventRepository(apiService)
        viewModel = ViewModelProvider(this, EventDetailViewModelFactory(repository))
            .get(EventDetailViewModel::class.java)
        viewModel.isloading.observe(viewLifecycleOwner) { isloading ->
            if (isloading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }


        Log.d("EventDetailFragment", "ViewModel berhasil diinisialisasi")

        // Memulai proses mendapatkan detail event dari ViewModel
        viewModel.getEventDetail(eventId.toInt())
        Log.d("EventDetailFragment", "Meminta detail event untuk ID: $eventId")

        // Mengamati LiveData dari ViewModel
        viewModel.detailEvent.observe(viewLifecycleOwner) { detailEvent ->
            Log.d("EventDetailFragment", "Detail event diterima: $detailEvent")

            detailEvent?.let { response ->
                if (response.error == true) {
                    response.message?.let { Log.e("EventDetailFragment", "Error: $it") }
                    return@let
                }

                val event = response.event
                Log.d("EventDetailFragment", "Event detail tidak null: $event")

                // Inisialisasi UI
                val tvEventName = view.findViewById<TextView>(R.id.tvEventName)
                val mediaCover = view.findViewById<ImageView>(R.id.mediaCover)
                val btnOpenLink = view.findViewById<Button>(R.id.btnOpenLink)
                val tvOwnerName = view.findViewById<TextView>(R.id.tvOwnerName)
                val tvBeginTime = view.findViewById<TextView>(R.id.tvBeginTime)
                val tvQuota = view.findViewById<TextView>(R.id.tvQuota)
                val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
                val tvSummary = view.findViewById<TextView>(R.id.tvSummary)
                val tvCityName = view.findViewById<TextView>(R.id.tvCityName)
                val tvEndTime = view.findViewById<TextView>(R.id.tvEndTime)
                val tvCategory = view.findViewById<TextView>(R.id.tvCategory)

                // Set data yang diterima dari ViewModel ke UI
                event?.let {
                    Log.d("EventDetailFragment", "Menampilkan nama event: ${it.name}")
                    tvEventName.text = it.name

                    Log.d("EventDetailFragment", "Menampilkan owner: ${it.ownerName}")
                    tvOwnerName.text = it.ownerName

                    Log.d("EventDetailFragment", "Menampilkan begin time: ${it.beginTime}")
                    tvBeginTime.text = it.beginTime

                    Log.d("EventDetailFragment", "Menampilkan summary: ${it.summary}")
                    tvSummary.text = it.summary

                    Log.d("EventDetailFragment", "Menampilkan city name: ${it.cityName}")
                    tvCityName.text = it.cityName

                    Log.d("EventDetailFragment", "Menampilkan end time: ${it.endTime}")
                    tvEndTime.text = it.endTime

                    Log.d("EventDetailFragment", "Menampilkan category: ${it.category}")
                    tvCategory.text = it.category

                    // Menampilkan informasi kuota dan registrants
                    val quota = it.quota
                    val registrants = it.registrants
                    Log.d("EventDetailFragment", "Menampilkan kuota: $quota, registrants: $registrants")
                    val quotaText = getString(R.string.quota_text, quota,
                        quota?.minus(registrants!!) ?: 0
                    )
                    tvQuota.text = quotaText

                    // Menampilkan deskripsi acara dengan HTML
                    Log.d("EventDetailFragment", "Menampilkan deskripsi")
                    tvDescription.text = HtmlCompat.fromHtml(
                        it.description ?: "",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    // Menampilkan gambar media cover menggunakan Glide
                    Log.d("EventDetailFragment", "Menampilkan gambar media cover: ${it.mediaCover}")
                    Glide.with(this)
                        .load(it.mediaCover)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(mediaCover)

                    // Tombol untuk membuka link event
                    btnOpenLink.setOnClickListener {
                        Log.d("EventDetailFragment", "Tombol link ditekan")
                        val link = event.link
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                }

            } ?: run {
                Log.e("EventDetailFragment", "Detail event is null")
            }
        }
    }
}
