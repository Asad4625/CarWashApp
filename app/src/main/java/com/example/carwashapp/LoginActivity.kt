package com.example.carwashapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carwashapp.Models.LoginResponse
import com.example.carwashapp.Models.User
import com.example.carwashapp.RetrofitSetup.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtSignup = findViewById<TextView>(R.id.txtGoToSignup)

        btnLogin.setOnClickListener {
            val user =
                User(Email = edtEmail.text.toString(), PasswordHash = edtPassword.text.toString())
            RetrofitClient.api.login(user).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        val intent = Intent(this@LoginActivity, BookingListActivity::class.java)
                        intent.putExtra("userId", res?.UserId)
                        intent.putExtra("role", res?.Role)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid Login", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        txtSignup.setOnClickListener { startActivity(Intent(this, SignupActivity::class.java)) }
    }
}