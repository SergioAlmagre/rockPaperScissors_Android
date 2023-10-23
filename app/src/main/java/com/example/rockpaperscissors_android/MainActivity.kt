package com.example.rockpaperscissors_android

import Api.ServiceBuilder
import Api.UserAPI
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rockpaperscissors_android.databinding.ActivityMainBinding
import Model.User
import android.content.Intent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEnter.setOnClickListener {

            val us = User(
                -1, binding.userNameInput.text.toString(),
                binding.passwordInput.text.toString(),-1
            )
            Log.e("Sergio", us.toString())

            val request = ServiceBuilder.buildService(UserAPI::class.java)
            val call = request.loginUser(us)
            var intentV1 = Intent(this, UsersListActivity::class.java)
            var intentV2 = Intent(this, GameActivity::class.java)

//            var intentV1 = Intent(this, Bienvenido::class.java)

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val post = response.body()
                    Log.e("Sergio",post.toString())

                    if (post != null && response.isSuccessful) {
                        if (post.rol==1){
                            intentV1.putExtra("actualUser",post)
                            startActivity(intentV1)
                        }else{
                            intentV2.putExtra("actualUser",post)
                            startActivity(intentV2)
                        }
                        Toast.makeText(this@MainActivity, "User found", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No results found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.btnPlay.setOnClickListener {
            var intentV2 = Intent(this@MainActivity, GameActivity::class.java)
            var guestUser = User(3,"guest","guest",0)
            intentV2.putExtra("guestUser",guestUser)
            startActivity(intentV2)
        }

    }
}