package apk.data.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apk.data.ui.adapter.EventAdapter
import apk.viewmodel.EventViewModel
import apk.viewmodel.EventViewModelFactory
import apk.data.repository.EventRepository
import apk.data.retrofit.ApiService
import com.DhikaWisata.wisatareview.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KegiatanAktifFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kegiatanaktif, container, false)

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
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

        // Observe LiveData dari ViewModel untuk acara aktif
        viewModel.getActiveEvents().observe(viewLifecycleOwner, Observer { events ->
            eventAdapter.updateData(events) // Update data adapter ketika ada perubahan data
        })

        return view
    }
}
