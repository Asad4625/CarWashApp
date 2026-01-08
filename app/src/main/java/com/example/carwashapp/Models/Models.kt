package com.example.carwashapp.Models

import com.google.gson.annotations.SerializedName

data class User(
    val UserId: Int? = null,
    val Name: String? = null,
    val Email: String,
    val PasswordHash: String,
    val Role: String? = null
)

data class LoginResponse(
    val UserId: Int,
    val Name: String,
    val Role: String
)

data class Booking(
    val BookingId: Int = 0,
    val UserId: Int,
    val CustomerName: String?, // New field
    val CustomerEmail: String?, // New field
    val ServiceType: String,
    val TimeSlot: String,
    val BookingDate: String,
    val TotalAmount: Double,
    val Status: String = "Pending"
)