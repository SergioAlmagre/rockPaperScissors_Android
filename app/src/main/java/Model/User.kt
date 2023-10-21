package Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
                @SerializedName("userId")
                val userId:Int? = null,

                @SerializedName("userName")
                val userName:String? = null,

                @SerializedName("password")
                val password:String? = null,

                @SerializedName("rol")
                val rol:Int? = null) :Serializable{
                }
