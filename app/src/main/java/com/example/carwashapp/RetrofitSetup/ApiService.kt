package com.example.carwashapp.RetrofitSetup

import com.example.carwashapp.Models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/signup")
    fun signup(@Body user: User): Call<ResponseBody>
    @POST("api/auth/login")
    fun login(@Body user: User): Call<LoginResponse>
    @GET("api/bookings/occupied-slots")
    fun getOccupiedSlots(@Query("date") date: String): Call<List<String>>
    @POST("api/bookings")
    fun createBooking(@Body booking: Booking): Call<Booking>
    @GET("api/bookings")
    fun getBookings(
        @Query("userId") userId: Int?,
        @Query("role") role: String
    ): Call<List<Booking>>

    @PUT("api/bookings/{id}")
    fun updateStatus(
        @Path("id") id: Int,
        @Body booking: Booking
    ): Call<ResponseBody>
}