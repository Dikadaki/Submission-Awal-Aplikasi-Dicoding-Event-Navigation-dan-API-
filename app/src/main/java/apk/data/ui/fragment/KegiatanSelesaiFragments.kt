package apk.data.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kegiatan_selesai, container, false)

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_selesai)
        recyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = EventAdapter(requireContext(), listOf())
        recyclerView.adapter = eventAdapter

        // Setup ViewModel
        val apiService = Retrofit.Builder()
            .baseUrl("https://event-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val repository = EventRepository(apiService)
        val factory = EventViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EventViewModel::class.java)

        // Observe LiveData dari ViewModel untuk acara selesai
        observeCompletedEvents()

        // Set header title
        val headerTitle = activity?.findViewById<TextView>(R.id.headerTitle)
        headerTitle?.text = getString(R.string.past_event_title)

        // Set item click listener
        eventAdapter.setOnItemClickListener { event ->
            // Panggil EventDetailFragment dengan mengirimkan EVENT_ID
            val bundle = Bundle().apply {
                putString("EVENT_ID", event.id.toString())
            }
            val eventDetailFragment = EventDetailFragment().apply {
                arguments = bundle
            }

            // Ganti fragment yang ada di dalam container dengan EventDetailFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, eventDetailFragment) // Ganti fragment di container
                .addToBackStack(null) // Tambah ke back stack agar bisa kembali
                .commit()
        }

        // Fetch completed events
        fetchCompletedEvents()

        return view
    }

    private fun observeCompletedEvents() {
        viewModel.completedEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.updateData(events) // Update data adapter ketika ada perubahan data
        }
    }


    private fun fetchCompletedEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                viewModel.fetchCompletedEvents() // Panggil fungsi fetchCompletedEvents di ViewModel
            } catch (e: Exception) {
                // Tangani kesalahan di sini (misalnya log atau tampilkan pesan kesalahan)
                e.printStackTrace()
            }
        }
    }
}
