package com.example.rockpaperscissors_android

import Api.ServiceBuilder
import Api.UserAPI
import InterConnection.InterConnections
import Model.User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rockpaperscissors_android.databinding.ActivityNewUserBinding
import com.example.rockpaperscissors_android.databinding.ActivityUserDetailsBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewUserActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        var context = this
        val builder = AlertDialog.Builder(context)

        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarNewUser.title = "    Details user"
        binding.toolbarNewUser.subtitle = "     admin mode"
        //        binding.toolbarUserDetails.setLogo(R.drawable.ic_logo)

        setSupportActionBar(binding.toolbarNewUser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarNewUser.setNavigationOnClickListener {
            finish()
        }


        binding.btnRegister.setOnClickListener {

            if(areFieldCorrect()){

                val request = ServiceBuilder.buildService(UserAPI::class.java)
                var newUser = User(
                    -1,
                    binding.txtUserNameNewUser.text.toString(),
                    binding.txtUserPasswordNewUser.text.toString(),
                    0
                )

                val call = request.addUser(newUser)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                context, "User added successfully", android.widget.Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Manejo de errores en caso de eliminación fallida
                            android.widget.Toast.makeText(
                                context, "The user couldn't be added", android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        android.widget.Toast.makeText(
                            context,
                            "Connection error",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

        }

    }
    fun clearFields(){
        binding.txtUserNameNewUser.setText("")
        binding.txtUserPasswordNewUser.setText("")
        binding.txtUserConfirmPassword.setText("")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menunewuser, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clearFieldsNewUser -> {
                clearFields()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun areFieldCorrect():Boolean{
        var isCorrect = true

        if ((binding.txtUserNameNewUser.text.toString().isEmpty()) )
        {
            binding.txtUserNameNewUser.setError("All fields must be filled")
            isCorrect = false
        }
        if ((binding.txtUserPasswordNewUser.text.toString().isEmpty()) )
        {
            binding.txtUserPasswordNewUser.setError("All fields must be filled")
            isCorrect = false
        }
        if ((binding.txtUserConfirmPassword.text.toString().isEmpty()) )
        {
            binding.txtUserConfirmPassword.setError("All fields must be filled")
            isCorrect = false
        }
        if(binding.txtUserPasswordNewUser.text.toString() != binding.txtUserConfirmPassword.text.toString())
        {
            binding.txtUserConfirmPassword.setError("Passwords must be the same")
            isCorrect = false
        }
        return isCorrect
    }

// NOT COMPLETE
    fun progressBarIncrement(){
        try {
            var progressValue = 0
            binding.txtUserNameNewUser.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    binding.progressBarNewUser.progress = progressValue++
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    binding.progressBarNewUser.progress = 0
                }
                override fun afterTextChanged(s: android.text.Editable) {
                    binding.progressBarNewUser.progress = progressValue++
                }
            })
            binding.txtUserPasswordNewUser.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    binding.progressBarNewUser.progress = progressValue++
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    binding.progressBarNewUser.progress = 0
                }
                override fun afterTextChanged(s: android.text.Editable) {
                    binding.progressBarNewUser.progress = progressValue++
                }
            })
            binding.txtUserConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    binding.progressBarNewUser.progress = progressValue++
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    binding.progressBarNewUser.progress = 0
                }
                override fun afterTextChanged(s: android.text.Editable) {
                    binding.progressBarNewUser.progress = progressValue++
                }
            })
        }catch (e:Exception) {
            Log.e("Sergio", e.toString())
        }
    }


}