package Api

import Model.Data
import Model.User
import Model.Result
import Model.Game
import Model.RecoveryGame
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.net.ResponseCache

interface UserAPI {

    @GET("list")
    fun getUserss(): Call<MutableList<User>>

    @GET("list/{userId}")
    fun getuser(@Path("userId") id:String): Call<User>

    @Headers("Content-Type:application/json")
    @POST("register")
    fun addUser(@Body info: User) : Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(@Body info: User) : Call<User>

    @Headers("Content-Type:application/json")
    @PUT("modify/{userId}")
    fun modUser(@Body info: User, @Path("userId") userId:String) : Call<ResponseBody>

    @DELETE("delete/{userId}")
    fun deleteUser(@Path("userId") userId:String) : Call<ResponseBody>

    //--------------------------------GAME------------------------------------------//
    @Headers("Content-Type:application/json")
    @PUT("modifygame/{gameId}")
    fun modifygame(@Body info: Game, @Path("gameId") gameId:String) : Call<ResponseBody>

    @GET("game/{gameId}")
    fun getgameId(@Path("gameId") id:String): Call<Game>

    @Headers("Content-Type:application/json")
    @POST("newgame")
    fun newGame(@Body info: Game) : Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("newresult")
    fun newResult(@Body info: Result) : Call<ResponseBody>

    @GET("getMaxGameId")
    fun getMaxGameId(): Call<Data>

    @GET("recovery/{userId}")
    fun recoveryGame(@Path("userId") id:Int): Call<RecoveryGame>
}