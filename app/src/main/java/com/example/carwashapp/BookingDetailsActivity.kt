package com.example.carwashapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carwashapp.Models.Booking
import com.example.carwashapp.RetrofitSetup.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.*
import java.util.*

class BookingDetailsActivity : AppCompatActivity() {

    private val services = mapOf(
        "Quick Wash ($10.0)" to 10.0,
        "Detailed Wash ($25.0)" to 25.0,
        "Premium Polish ($50.0)" to 50.0
    )
    private var selectedDate = ""
    private var currentBookingId: Int = 0
    private var isFinalized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        val uId = intent.getIntExtra("userId", 0)
        val role = intent.getStringExtra("role") ?: "Customer"

        // UI Elements
        val spService = findViewById<Spinner>(R.id.spinnerService)
        val spTime = findViewById<Spinner>(R.id.spinnerTime)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnDate = findViewById<Button>(R.id.btnDate)
        val txtDate = findViewById<TextView>(R.id.txtSelectedDate)

        // Spinner Setup
        spService.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, services.keys.toList())

        // Intent se data receive karna (Existing booking ke liye)
        val existingStatus = intent.getStringExtra("status")
        currentBookingId = intent.getIntExtra("bookingId", 0)

        // Agar status pehle se Completed/Cancelled hai to edit band kar dein
        if (existingStatus == "Completed" || existingStatus == "Cancelled") {
            isFinalized = true
            btnSave.isEnabled = false
            btnSave.alpha = 0.5f
            btnDate.isEnabled = false
            Toast.makeText(this, "Finalized bookings cannot be modified", Toast.LENGTH_LONG).show()
        }

        // Date Picker
        btnDate.setOnClickListener {
            if (isFinalized) return@setOnClickListener
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                txtDate.text = selectedDate
                fetchSlots(selectedDate, spTime)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Save Button Logic
        btnSave.setOnClickListener {
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedKey = spService.selectedItem.toString()
            val serviceName = selectedKey.substringBefore(" (")
            val price = services[selectedKey] ?: 0.0

            val booking = Booking(
                BookingId = currentBookingId,
                UserId = uId,
                ServiceType = serviceName,
                TimeSlot = spTime.selectedItem.toString(),
                BookingDate = selectedDate,
                TotalAmount = price,
                Status = if (role == "Staff") "Completed" else "Pending",
                CustomerName = null, // Backend handle karega
                CustomerEmail = null
            )

            if (currentBookingId == 0) {
                // CASE 1: New Booking (Expects Booking Response)
                RetrofitClient.api.createBooking(booking).enqueue(object : Callback<Booking> {
                    override fun onResponse(call: Call<Booking>, r: Response<Booking>) {
                        if (r.isSuccessful) {
                            Toast.makeText(this@BookingDetailsActivity, "Booking Created!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<Booking>, t: Throwable) {
                        Toast.makeText(this@BookingDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                              RetrofitClient.api.updateStatus(currentBookingId, booking).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, r: Response<ResponseBody>) {
                        if (r.isSuccessful) {
                            Toast.makeText(this@BookingDetailsActivity, "Status Updated!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@BookingDetailsActivity, "Update Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun fetchSlots(date: String, spinner: Spinner) {
        RetrofitClient.api.getOccupiedSlots(date).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, r: Response<List<String>>) {
                val slots = listOf("09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM")
                val occupied = r.body() ?: emptyList()
                val available = slots.filter { !occupied.contains(it) }
                spinner.adapter = ArrayAdapter(this@BookingDetailsActivity, android.R.layout.simple_spinner_dropdown_item, available)
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {}
        })
    }
}