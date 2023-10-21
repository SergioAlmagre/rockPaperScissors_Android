package Adapters

import Api.ServiceBuilder
import Api.UserAPI
import InterConnection.InterConnections
import Model.User
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors_android.R
import com.example.rockpaperscissors_android.UserDetails
import com.example.rockpaperscissors_android.UsersListActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MyAdapterRV (private var context: Context,
                   private var users : ArrayList<User>
) :
RecyclerView.Adapter<MyAdapterRV.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(v)
    }

    val builder = AlertDialog.Builder(context)
//    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
//        Toast.makeText(context, "Has pulsado sí", Toast.LENGTH_SHORT).show()
//    }
//    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
//        Toast.makeText(context, "Has pulsado no", Toast.LENGTH_SHORT).show()
//    }
//    val neutralButtonClick = { dialog: DialogInterface, which: Int ->
//        Toast.makeText(context, "Quizá", Toast.LENGTH_SHORT).show()
//    }


    override fun getItemCount(): Int {
        return users.size
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView = itemView.findViewById<View>(R.id.txtId) as TextView
        var name: TextView = itemView.findViewById<View>(R.id.txtName) as TextView
    }


    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.id.text = users[position].userId.toString()
        holder.name.text = users[position].userName.toString()

        holder.itemView.setOnClickListener {
            Toast.makeText(context, users[position].userId.toString(), Toast.LENGTH_SHORT).show()

            val us = InterConnections.AllUsersIC[position]
            Log.e("Sergio", us.toString())

            var intentV1 = Intent(context, UserDetails::class.java)

            intentV1.putExtra("userPressed",us)
            context.startActivity(intentV1)

        }

        holder.itemView.setOnLongClickListener {

            with(builder)
            {
                setTitle("Delete")
                setMessage("This will delete the user. Are you sure?")
                setPositiveButton("Yes", DialogInterface.OnClickListener(function = { dialog: DialogInterface, which: Int ->
                    val request = ServiceBuilder.buildService(UserAPI::class.java)
                    val call = request.deleteUser(users[position].userId.toString())

                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {

                                Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
                                users.removeAt(position)
                                notifyDataSetChanged()

                            } else {
                                // Manejo de errores en caso de eliminación fallida
                                Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // Error de conexión
                            Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
                        }
                    })

                }))
                setNegativeButton("No", ({ dialog: DialogInterface, which: Int ->
//                                Toast.makeText(context, "Has pulsado no", Toast.LENGTH_SHORT).show()
                }))
                show()
            }

            true
        }

    }



}