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
import com.DhikaWisata.wisatareview.R

class KegiatanSelesaiFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kegiatan_selesai, container, false)

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_selesai)
        recyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = EventAdapter(requireContext(), listOf())
        recyclerView.adapter = eventAdapter

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Observe data dari ViewModel (event selesai)
        viewModel.getCompletedEvents().observe(viewLifecycleOwner, Observer { events ->
            eventAdapter = EventAdapter(requireContext(), events)
            recyclerView.adapter = eventAdapter
        })

        return view
    }
}
