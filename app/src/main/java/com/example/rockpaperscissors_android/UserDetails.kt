package com.example.rockpaperscissors_android

import Api.ServiceBuilder
import Api.UserAPI
import InterConnection.InterConnections
import Model.User
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rockpaperscissors_android.databinding.ActivityUserDetailsBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetails : AppCompatActivity() {
    lateinit var binding: ActivityUserDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_user_details)

        var context = this
        val builder = AlertDialog.Builder(context)

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var user = intent.getSerializableExtra("userPressed") as User

        Log.e("Sergio", user.toString() + "este error en intent details")

        binding.txtUserId.setText(user.userId.toString())
        binding.txtUserName.setText(user.userName)
        binding.txtUserPassword.setText(user.password)
        binding.txtUserRol.setText(user.rol.toString())

        binding.toolbarUserDetails.title = "    Details user"
        binding.toolbarUserDetails.subtitle = "     admin mode"
        //        binding.toolbarUserDetails.setLogo(R.drawable.ic_logo)

        setSupportActionBar(binding.toolbarUserDetails)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarUserDetails.setNavigationOnClickListener {
            finish()
        }



        binding.btnSaveChanges.setOnClickListener {

            with(builder)
            {
                setTitle("Modify")
                setMessage("This will modify the user. Are you sure?")
                setPositiveButton(
                    "Yes",
                    android.content.DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                        val request = Api.ServiceBuilder.buildService(Api.UserAPI::class.java)
                        var newUser = User(
                            binding.txtUserId.text.toString().toInt(),
                            binding.txtUserName.text.toString(),
                            binding.txtUserPassword.text.toString(),
                            binding.txtUserRol.text.toString().toInt()
                        )
                        val call = request.modUser(newUser, newUser.userId.toString())

                        call.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {

                                    Toast.makeText(
                                        context,
                                        "User Modified",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                    InterConnections.AllUsersIC[user.userId!!] = user

                                } else {
                                    // Manejo de errores en caso de eliminación fallida
                                    android.widget.Toast.makeText(
                                        context,
                                        "Error deleting user",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                // Error de conexión
                                android.widget.Toast.makeText(
                                    context,
                                    "Connection error",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                    })
                )
                setNegativeButton("No", ({ dialog: DialogInterface, which: Int ->
//                                Toast.makeText(context, "Has pulsado no", Toast.LENGTH_SHORT).show()
                }))
                show()
            }


        }

    }

}