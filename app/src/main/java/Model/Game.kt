package Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Game(@SerializedName("gameId")
                val gameId:Int? = null,

                @SerializedName("gameNumber")
                val gameNumber:Int? = null,

                @SerializedName("playerId")
                val playerId:Int? = null,

                @SerializedName("idHand")
                val idHand:Int? = null ,

                @SerializedName("handWon")
                val handsWon:Int? = null):Serializable{
}