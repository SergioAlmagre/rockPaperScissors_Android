package Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Result(@SerializedName("gameNumber")
                   val gameNumber:Int? = null,

                  @SerializedName("playerWinnerId")
                   val playerWinnerId:Int? = null,

                  @SerializedName("gameOver")
                   val gameOver:Int? = null ): Serializable {

}