package apk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apk.data.repository.EventRepository
import apk.data.response.ListEventsItem


class EventViewModel(private val repository: EventRepository) : ViewModel() {

    // Mengambil daftar acara aktif dari repository
    fun getActiveEvents(): LiveData<List<ListEventsItem>> {
        return repository.getEvents() // Memanggil fungsi dari repository
    }

    // Jika Anda ingin menambahkan fungsi untuk acara selesai
    fun getCompletedEvents(): LiveData<List<ListEventsItem>> {
        return repository.getCompletedEvents() // Memanggil fungsi dari repository
    }
}

// Factory untuk membuat instance EventViewModel
class EventViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}