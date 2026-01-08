package com.example.carwashapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carwashapp.Models.User
import com.example.carwashapp.RetrofitSetup.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val spinnerRole = findViewById<Spinner>(R.id.spinnerRole)
        spinnerRole.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Customer", "Staff"))

        findViewById<Button>(R.id.btnSignup).setOnClickListener {
            val user = User(
                Name = findViewById<EditText>(R.id.edtName).text.toString(),
                Email = findViewById<EditText>(R.id.edtEmail).text.toString(),
                PasswordHash = findViewById<EditText>(R.id.edtPassword).text.toString(),
                Role = spinnerRole.selectedItem.toString()
            )

            RetrofitClient.api.signup(user).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SignupActivity, "Signup Success!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
        }
    }
}