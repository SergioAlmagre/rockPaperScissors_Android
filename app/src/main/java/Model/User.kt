package Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * userId: -1 = new user, 0 = normal player, 1 = admin 2 = machine
 */
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
