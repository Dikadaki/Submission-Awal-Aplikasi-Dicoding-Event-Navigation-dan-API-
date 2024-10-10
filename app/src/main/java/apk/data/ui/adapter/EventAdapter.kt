package apk.data.ui.adapter

import android.content.Context
import android.widget.ImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apk.data.response.ListEventsItem
import com.DhikaWisata.wisatareview.R

class EventAdapter(
    private val context: Context,
    private var events: List<ListEventsItem>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.nameText.text = event.name

        Glide.with(context)
            .load(event.mediaCover)
            .placeholder(R.drawable.placeholder) // Placeholder image
            .into(holder.itemImage)
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_Image)
        val nameText: TextView = itemView.findViewById(R.id.name_Text)
    }
    fun updateData(newEvents: List<ListEventsItem>) {
        events = newEvents
        notifyDataSetChanged() // Memberitahu RecyclerView untuk memperbarui tampilannya
    }
}
