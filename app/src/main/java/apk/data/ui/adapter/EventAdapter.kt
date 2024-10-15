package apk.data.ui.adapter // Mendeklarasikan paket tempat kelas ini berada

import android.content.Context // Mengimpor kelas Context dari Android
import android.widget.ImageView // Mengimpor kelas ImageView untuk menampilkan gambar
import android.view.LayoutInflater // Mengimpor LayoutInflater untuk mengatur layout
import android.view.View // Mengimpor kelas View
import android.view.ViewGroup // Mengimpor kelas ViewGroup
import com.bumptech.glide.Glide // Mengimpor Glide untuk memuat gambar
import android.widget.TextView // Mengimpor kelas TextView untuk menampilkan teks
import androidx.recyclerview.widget.RecyclerView // Mengimpor RecyclerView untuk menampilkan daftar
import apk.data.response.ListEventsItem // Mengimpor ListEventsItem yang berisi data event
import com.DhikaWisata.wisatareview.R // Mengimpor sumber daya dari aplikasi

// Kelas EventAdapter yang mengatur tampilan item dalam RecyclerView
class EventAdapter(
    private val context: Context, // Konteks untuk mengakses sumber daya
    private var events: List<ListEventsItem> // Daftar event yang akan ditampilkan
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() { // Mewarisi RecyclerView.Adapter dengan ViewHolder khusus

    // Variabel untuk menyimpan listener klik item
    private var onItemClickListener: ((ListEventsItem) -> Unit)? = null

    // Metode untuk mengatur listener klik item
    fun setOnItemClickListener(listener: (ListEventsItem) -> Unit) {
        onItemClickListener = listener // Menyimpan listener yang diberikan
    }

    // Metode untuk membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        // Meng-inflate layout item_event menjadi View
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view) // Mengembalikan ViewHolder baru
    }

    // Metode untuk mengikat data ke ViewHolder
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position] // Mengambil event berdasarkan posisi
        holder.nameText.text = event.name // Mengatur nama event ke TextView

        // Mengatur listener klik pada item view
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(event) } // Memanggil listener jika ada
        }

        // Memuat gambar dengan Glide
        Glide.with(context)
            .load(event.mediaCover) // Mengambil URL gambar dari event
            .placeholder(R.drawable.placeholder) // Gambar placeholder saat loading
            .error(R.drawable.error) // Gambar jika terjadi error saat memuat
            .into(holder.itemImage) // Memasukkan gambar ke ImageView
    }

    // Metode untuk mendapatkan jumlah item dalam daftar
    override fun getItemCount() = events.size // Mengembalikan ukuran daftar events

    // Kelas ViewHolder untuk menampung tampilan item
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_Image) // Mengambil ImageView dari layout
        val nameText: TextView = itemView.findViewById(R.id.name_Text) // Mengambil TextView dari layout
    }

    // Metode untuk memperbarui data event
    fun updateData(newEvents: List<ListEventsItem>) {
        val oldSize = events.size // Menyimpan ukuran lama daftar events
        events = newEvents // Memperbarui daftar events dengan yang baru
        if (oldSize == 0) {
            notifyItemRangeInserted(0, newEvents.size) // Notifikasi untuk item baru jika sebelumnya kosong
        } else {
            notifyItemRangeChanged(0, newEvents.size) // Notifikasi jika data sudah ada
        }
    }
}
