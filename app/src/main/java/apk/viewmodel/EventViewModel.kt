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

    private val  _isloading = MutableLiveData<Boolean>()
    val isloading: LiveData<Boolean> get() = _isloading

    private val _activeEvent = MutableLiveData<List<ListEventsItem>>()
    val activeEvent: LiveData<List<ListEventsItem>> get() = _activeEvent

    private val _completedEvents = MutableLiveData<List<ListEventsItem>>()
    val completedEvents: LiveData<List<ListEventsItem>> get() = _completedEvents

    // Fungsi untuk mengambil acara aktif
    fun fetchActiveEvents() {
        viewModelScope.launch {
            _isloading.value = true  // Set isLoading menjadi true saat mulai mengambil data
            try {
                // Mengambil data acara aktif dari repository
                _activeEvent.value = repository.getActiveEvents()
            } catch (e: Exception) {
                // Tangani error jika ada
            } finally {
                _isloading.value = false  // Set isLoading menjadi false setelah data diambil
            }
        }
    }

    // Fungsi untuk mengambil acara selesai
    fun fetchCompletedEvents() {
        viewModelScope.launch {
            _isloading.value = true  // Set isLoading menjadi true saat mulai mengambil data
            try {
                // Mengambil data acara selesai dari repository
                _completedEvents.value = repository.getCompletedEvents()
            } catch (e: Exception) {
                // Tangani error jika ada
            } finally {
                _isloading.value = false  // Set isLoading menjadi false setelah data diambil
            }
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
