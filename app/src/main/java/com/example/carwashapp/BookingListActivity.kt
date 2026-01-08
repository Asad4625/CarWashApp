package com.example.carwashapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.Models.Booking
import com.example.carwashapp.RetrofitSetup.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.ResponseBody
import retrofit2.*

class BookingListActivity : AppCompatActivity() {
    private var userId: Int = 0
    private var role: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_list)

        userId = intent.getIntExtra("userId", 0)
        role = intent.getStringExtra("role") ?: "Customer"

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            val i = Intent(this, BookingDetailsActivity::class.java)
            i.putExtra("userId", userId)
            i.putExtra("role", role) // Role pass karna zaroori hai
            startActivity(i)
        }
        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        RetrofitClient.api.getBookings(userId, role).enqueue(object : Callback<List<Booking>> {
            override fun onResponse(call: Call<List<Booking>>, response: Response<List<Booking>>) {
                if (response.isSuccessful) {
                    val rv = findViewById<RecyclerView>(R.id.recyclerBookings)
                    rv.layoutManager = LinearLayoutManager(this@BookingListActivity)

                    val bookings = response.body() ?: emptyList()
                    rv.adapter = BookingAdapter(bookings, role) { booking ->
                        if (role == "Staff") {
                            if (booking.Status == "Completed" || booking.Status == "Cancelled") {
                                Toast.makeText(this@BookingListActivity, "Already Finalized", Toast.LENGTH_SHORT).show()
                            } else {
                                showUpdateDialog(booking)
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Booking>>, t: Throwable) {
                Toast.makeText(this@BookingListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showUpdateDialog(booking: Booking) {
        val options = arrayOf("In Progress", "Completed", "Cancelled")
        AlertDialog.Builder(this)
            .setTitle("Update Status")
            .setItems(options) { _, which ->
                val newStatus = options[which]

                // Create a booking object to match C# API [FromBody] Bookings
                val updateRequest = Booking(
                    BookingId = booking.BookingId,
                    UserId = booking.UserId,
                    ServiceType = booking.ServiceType,
                    TimeSlot = booking.TimeSlot,
                    BookingDate = booking.BookingDate,
                    TotalAmount = booking.TotalAmount,
                    Status = newStatus,
                    CustomerName = null,
                    CustomerEmail = null
                )

                RetrofitClient.api.updateStatus(booking.BookingId, updateRequest).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, r: Response<ResponseBody>) {
                        if (r.isSuccessful) {
                            Toast.makeText(this@BookingListActivity, "Status updated to $newStatus", Toast.LENGTH_SHORT).show()
                            loadData() // Refresh list
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@BookingListActivity, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }.show()
    }
}