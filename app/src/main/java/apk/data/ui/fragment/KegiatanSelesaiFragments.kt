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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KegiatanSelesaiFragments : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: EventViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kegiatan_selesai, container, false)

        Log.d("KegiatanSelesaiFragments", "onCreateView: Inisialisasi fragment selesai")

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_selesai)
        recyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = EventAdapter(requireContext(), listOf())
        recyclerView.adapter = eventAdapter

        Log.d("KegiatanSelesaiFragments", "RecyclerView dan EventAdapter berhasil diinisialisasi")

        // Setup ViewModel
        val apiService = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val repository = EventRepository(apiService)
        val factory = EventViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EventViewModel::class.java)

        Log.d("KegiatanSelesaiFragments", "ViewModel berhasil diinisialisasi")

        // Observe LiveData dari ViewModel untuk acara selesai
        observeCompletedEvents()

        // Set header title
        val headerTitle = activity?.findViewById<TextView>(R.id.headerTitle)
        headerTitle?.text = getString(R.string.past_event_title)

        Log.d("KegiatanSelesaiFragments", "Header title diatur ke: ${getString(R.string.past_event_title)}")

        // Set item click listener
        eventAdapter.setOnItemClickListener { event ->
            Log.d("KegiatanSelesaiFragments", "Item di klik: ${event.name}, ID: ${event.id}")

            // Panggil EventDetailFragment dengan mengirimkan EVENT_ID
            val bundle = Bundle().apply {
                putString("EVENT_ID", event.id.toString())
            }
            val eventDetailFragment = EventDetailFragment().apply {
                arguments = bundle
            }

            // Ganti fragment yang ada di dalam container dengan EventDetailFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, eventDetailFragment)
                .addToBackStack(null)
                .commit()

            Log.d("KegiatanSelesaiFragments", "EventDetailFragment berhasil diinstal dengan ID: ${event.id}")
        }

        // Fetch completed events
        fetchCompletedEvents()

        return view
    }

    private fun observeCompletedEvents() {
        viewModel.completedEvents.observe(viewLifecycleOwner) { events ->
            Log.d("KegiatanSelesaiFragments", "Jumlah acara selesai yang diterima: ${events.size}")

            eventAdapter.updateData(events) // Update data adapter ketika ada perubahan data
        }
    }

    private fun fetchCompletedEvents() {
        Log.d("KegiatanSelesaiFragments", "Fetching acara selesai dimulai")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                viewModel.fetchCompletedEvents()
                Log.d("KegiatanSelesaiFragments", "Fetching acara selesai berhasil")
            } catch (e: Exception) {
                // Tangani kesalahan di sini (misalnya log atau tampilkan pesan kesalahan)
                Log.e("KegiatanSelesaiFragments", "Error saat fetching acara selesai: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        progressBar = view.findViewById(R.id.progressBar)

        viewModel.isloading.observe(viewLifecycleOwner) {isloading ->
            Log.d("KegiatanSelesaiFragment", "isLoading: $isloading")
            if (isloading){
                progressBar.visibility = View.VISIBLE
            }else {
                progressBar.visibility = View.GONE
            }
        }
        viewModel.fetchActiveEvents()
    }
}
