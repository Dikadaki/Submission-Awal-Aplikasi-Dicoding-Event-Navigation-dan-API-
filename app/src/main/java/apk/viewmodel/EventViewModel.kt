package apk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import apk.data.repository.EventRepository
import apk.data.response.ListEventsItem
import kotlinx.coroutines.launch

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    private val _activeEvent = MutableLiveData<List<ListEventsItem>>()
    val activeEvent: LiveData<List<ListEventsItem>> get() = _activeEvent

    private val _completedEvents = MutableLiveData<List<ListEventsItem>>()
    val completedEvents: LiveData<List<ListEventsItem>> get() = _completedEvents

    // Fungsi untuk mengambil acara aktif
    fun fetchActiveEvents() {
        viewModelScope.launch {
            // Mengambil data acara aktif dari repository
            _activeEvent.value = repository.getActiveEvents()
        }
    }

    // Fungsi untuk mengambil acara selesai
    fun fetchCompletedEvents() {
        viewModelScope.launch {
            // Mengambil data acara selesai dari repository
            _completedEvents.value = repository.getCompletedEvents()
        }
    }
}

// Factory untuk membuat instance EventViewModel
class EventViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
