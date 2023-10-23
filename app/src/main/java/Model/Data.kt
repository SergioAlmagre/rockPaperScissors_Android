package Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(@SerializedName("maxGameId")
                val maxGameId:Int): Serializable{
}