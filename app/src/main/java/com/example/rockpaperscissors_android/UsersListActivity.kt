package com.example.rockpaperscissors_android

import Adapters.MyAdapterRV
import Api.ServiceBuilder
import Api.UserAPI
import InterConnection.InterConnections
import Model.User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors_android.databinding.ActivityListadoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersListActivity : AppCompatActivity() {
    var users: ArrayList<User> = ArrayList()

    lateinit var recyclerView : RecyclerView
    lateinit var binding: ActivityListadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_listado)
        binding = ActivityListadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarListView.title = "    All users"
        binding.toolbarListView.subtitle = "     admin mode"
        //        binding.toolbarUserDetails.setLogo(R.drawable.ic_logo)

        setSupportActionBar(binding.toolbarListView)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarListView.setNavigationOnClickListener {
            finish()
        }


        recyclerView = findViewById<RecyclerView>(R.id.RVListaPersonas)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager

        var user = intent.getSerializableExtra("actualUser") as User

        getUsers()

    }

    /**
     * Acceso a los usuarios sin viewModel.
     */
    fun getUsers(){
        val request = ServiceBuilder.buildService(UserAPI::class.java)
        val call = request.getUserss()

        call.enqueue(object : Callback<MutableList<User>> {
            override fun onResponse(call: Call<MutableList<User>>, response: Response<MutableList<User>>) {
                Log.e ("Sergio", response.code().toString())
                for (post in response.body()!!) {
                    InterConnections.AllUsersIC.add(User(post.userId,post.userName,post.password,post.rol))
                }
                if (response.isSuccessful){
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@UsersListActivity)
                        adapter = MyAdapterRV(this@UsersListActivity, InterConnections.AllUsersIC)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                Toast.makeText(this@UsersListActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menulistview, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newUserMenu -> {
                var intentV1 = Intent(this, NewUserActivity::class.java)
                startActivity(intentV1)

            }
            R.id.exitMenu -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



}