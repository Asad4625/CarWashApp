package com.example.carwashapp

import android.graphics.Color
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.Models.Booking


class BookingAdapter(
    private val list: List<Booking>,
    private val role: String,
    val onClick: (Booking) -> Unit
) : RecyclerView.Adapter<BookingAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val s: TextView = v.findViewById(R.id.txtService)
        val dt: TextView = v.findViewById(R.id.txtDateTime)
        val st: TextView = v.findViewById(R.id.txtStatus)
        val custInfo: TextView = v.findViewById(R.id.txtCustomerInfo)
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int): VH {
        val view = LayoutInflater.from(p.context).inflate(R.layout.row_booking, p, false)
        return VH(view)
    }

    override fun onBindViewHolder(h: VH, p: Int) {
        val b = list[p]
        h.s.text = b.ServiceType
        h.dt.text = "${b.BookingDate.take(10)} | ${b.TimeSlot}"
        h.st.text = b.Status

        // Status Colors
        when (b.Status) {
            "Completed" -> h.st.setTextColor(Color.parseColor("#2E7D32"))
            "Cancelled" -> h.st.setTextColor(Color.parseColor("#C62828"))
            else -> h.st.setTextColor(Color.parseColor("#1565C0"))
        }


        if (role == "Staff") {
            h.custInfo.visibility = View.VISIBLE
            val name = b.CustomerName ?: "Name Not Received"
            val email = b.CustomerEmail ?: "Email Not Received"
            h.custInfo.text = "Customer: $name\n$email"
        } else {
            h.custInfo.visibility = View.GONE
        }

        h.itemView.setOnClickListener { onClick(b) }
    }

    override fun getItemCount() = list.size
}