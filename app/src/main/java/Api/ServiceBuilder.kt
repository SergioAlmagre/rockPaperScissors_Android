package Api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    val client = OkHttpClient.Builder().build()
    val url = "http://192.168.1.136:8080"


    private val retrofit = Retrofit.Builder()
        .baseUrl(url) //Con 127.0.0.1 hay un problema de seguridad. Se debe acceder por esta ip (especial para enmascarar localhost) que accede a localhost.
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }

    fun buidPostService(cuerpo: RequestBody): Request {
        return Request.Builder().url(url)
            .post(cuerpo)
            .build()
    }
}