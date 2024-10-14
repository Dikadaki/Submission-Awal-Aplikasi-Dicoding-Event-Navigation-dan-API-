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
        eventId = arguments?.getString("EVENT_ID") ?: return null

        // Mengatur layout fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Event ID", "eventId: $eventId")

        // Setup Retrofit dan ApiService
        val retrofit = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Inisialisasi repository dan ViewModel
        val repository = EventRepository(apiService)
        viewModel = ViewModelProvider(this, EventDetailViewModelFactory(repository))
            .get(EventDetailViewModel::class.java)


        // Memulai proses mendapatkan detail event dari ViewModel
        viewModel.getEventDetail(eventId.toInt())

        // Mengamati LiveData dari ViewModel
        viewModel.detailEvent.observe(viewLifecycleOwner) { detailEvent ->
            Log.d("EventDetailFragment", "Detail Event: $detailEvent")
            detailEvent?.let { response ->
                if (response.error) {
                    Log.e("EventDetailActivity", response.message)
                    // Tampilkan pesan error ke pengguna jika perlu
                    return@let
                }

                // Mengakses event dari response
                val event = response.event

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
                tvEventName.text = event.name
                tvOwnerName.text = event.ownerName
                tvBeginTime.text = event.beginTime
                tvSummary.text = event.summary
                tvCityName.text = event.cityName
                tvEndTime.text = event.endTime
                tvCategory.text = event.category

                // Menampilkan informasi kuota dan registrants
                val quota = event.quota
                val registrants = event.registrants
                val quotaText = getString(R.string.quota_text, quota, quota - registrants)
                tvQuota.text = quotaText

                // Menampilkan deskripsi acara dengan HTML
                tvDescription.text = HtmlCompat.fromHtml(
                    event.description ,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

                // Menampilkan gambar media cover menggunakan Glide
                Glide.with(this)
                    .load(event.mediaCover) // Menggunakan mediaCover dari response
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(mediaCover)

                // Tombol untuk membuka link event
                btnOpenLink.setOnClickListener {
                    val link = event.link // Menggunakan it.link
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                }

            } ?: run {
                // Tangani jika detailEvent null
                Log.e("EventDetailFragment", "Detail event is null")
            }
        }
    }
}
