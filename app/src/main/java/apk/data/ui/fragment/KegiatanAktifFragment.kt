package apk.data.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apk.data.ui.adapter.EventAdapter
import apk.viewmodel.EventViewModel
import apk.viewmodel.EventViewModelFactory
import apk.data.repository.EventRepository
import apk.data.retrofit.ApiService
import apk.ui.EventDetailFragment
import com.DhikaWisata.wisatareview.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KegiatanAktifFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: EventViewModel
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kegiatanaktif, container, false)

        Log.d("KegiatanAktifFragment", "onCreateView: Inisialisasi fragment aktif")

        // Mengatur judul header
        val headerTitle = activity?.findViewById<TextView>(R.id.headerTitle)
        headerTitle?.text = getString(R.string.active_event_title)
        Log.d("KegiatanAktifFragment", "Judul header diatur ke: ${getString(R.string.active_event_title)}")

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = EventAdapter(requireContext(), listOf())
        recyclerView.adapter = eventAdapter

        Log.d("KegiatanAktifFragment", "RecyclerView dan EventAdapter berhasil diinisialisasi")

        // Setup ViewModel
        val apiService = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val repository = EventRepository(apiService)
        val factory = EventViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EventViewModel::class.java)

        Log.d("KegiatanAktifFragment", "ViewModel berhasil diinisialisasi")

        // Memanggil metode fetch untuk mendapatkan acara aktif
        Log.d("KegiatanAktifFragment", "Memulai fetching acara aktif")
        viewModel.fetchActiveEvents()

        // Observe LiveData dari ViewModel untuk acara aktif
        viewModel.activeEvent.observe(viewLifecycleOwner) { events ->
            Log.d("KegiatanAktifFragment", "Jumlah acara aktif yang diterima: ${events.size}")
            eventAdapter.updateData(events) // Update data adapter ketika ada perubahan data
        }

        // Set listener untuk item click
        eventAdapter.setOnItemClickListener { event ->
            Log.d("KegiatanAktifFragment", "Item di klik: ${event.name}, ID: ${event.id}")

            // Mengirimkan data event ID ke EventDetailFragment
            val eventDetailFragment = EventDetailFragment()
            val bundle = Bundle()
            bundle.putString("EVENT_ID", event.id.toString()) //
            Log.d("KegiatanAktifFragment", "Berhasil mengirim EVENT_ID: ${event.id} ke EventDetailFragment")
            // Mengirim event ID
            eventDetailFragment.arguments = bundle

            // Mengganti fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, eventDetailFragment) // Mengganti fragment
                .addToBackStack(null) // Menambah ke back stack untuk kembali ke fragment sebelumnya
                .commit()

            Log.d("KegiatanAktifFragment", "Berhasil mengganti fragment ke EventDetailFragment dengan ID: ${event.id}")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        progressBar = view.findViewById(R.id.progressBar)

        viewModel.isloading.observe(viewLifecycleOwner) {isloading ->
            Log.d("KegiatanAktifFragment", "isLoading: $isloading")
            if (isloading){
                progressBar.visibility = View.VISIBLE
            }else {
                progressBar.visibility = View.GONE
            }
        }
        viewModel.fetchActiveEvents()
    }
}
