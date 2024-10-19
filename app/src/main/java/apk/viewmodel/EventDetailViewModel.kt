package apk.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apk.data.repository.EventRepository
import apk.data.response.DetailEventResponse
import kotlinx.coroutines.launch

// Kelas EventDetailViewModel mengelola data dan logika terkait detail acara
class EventDetailViewModel(private val repository: EventRepository) : ViewModel() {
    val isloading = MutableLiveData<Boolean>()

    // MutableLiveData untuk menyimpan detail acara
    private val _detailEvent = MutableLiveData<DetailEventResponse?>()

    // LiveData untuk diakses oleh UI (activity/fragment)
    val detailEvent: LiveData<DetailEventResponse?> = _detailEvent

    // MutableLiveData untuk menyimpan pesan kesalahan
    private val _errorMessage = MutableLiveData<String?>()

    // Fungsi untuk mendapatkan detail acara berdasarkan ID
    fun getEventDetail(id: Int) {
        // Log saat fungsi dipanggil
        Log.d("EventDetailViewModel", "getEventDetail() dipanggil dengan ID: $id")

        // Meluncurkan coroutine di dalam viewModelScope
        viewModelScope.launch {
            isloading.value = true
            try {
                // Mengambil data dari repository
                Log.d("EventDetailViewModel", "Mengambil detail event dari repository")
                val response: DetailEventResponse? = repository.getEventDetail(id)

                // Memeriksa apakah response tidak null dan tidak ada kesalahan
                if (response != null && !response.error!!) {
                    // Jika tidak ada kesalahan, set nilai ke LiveData
                    Log.d("EventDetailViewModel", "Detail event berhasil diambil: $response")
                    _detailEvent.value = response
                } else {
                    // Jika ada kesalahan, set pesan kesalahan ke LiveData
                    val errorMessage = response?.message ?: "Terjadi kesalahan"
                    Log.e("EventDetailViewModel", "Kesalahan dalam respons: $errorMessage")
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                // Menangkap kesalahan dan set pesan kesalahan ke LiveData
                Log.e("EventDetailViewModel", "Error saat mengambil detail event: ${e.message}")
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                // Pastikan loading dihentikan di blok finally
                isloading.value= false
            }

        }
    }
}
