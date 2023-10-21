package modelo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Game(@SerializedName("gameId")
                val gameId:Int? = null,

                @SerializedName("playerId")
                val playerId:Int? = null,

                @SerializedName("hand")
                val hand:Int? = null ,

                @SerializedName("handsWon")
                val handsWon:Int? = null,

                @SerializedName("gameWon")
                val gameWon:Int? = null):Serializable{
}