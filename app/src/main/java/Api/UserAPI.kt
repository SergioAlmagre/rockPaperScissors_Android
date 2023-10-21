package Api

import Model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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
}