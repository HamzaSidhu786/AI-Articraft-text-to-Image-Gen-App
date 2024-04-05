package com.hamza.aiarticraft.ui.api

import com.google.gson.JsonArray
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {
//    @POST("sdapi/v1/txt2img")
    @POST("v1/stable-diffusion/text-to-image")
    fun sendRequest(@Body request: RequestBody): Call<ApiResponse>
}
object ApiInstance {
    private const val BASE_URL = "https://api.getimg.ai/"

    private val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "<<<REPLACE YOUR KEY HERE>>>")
            .build()
        chain.proceed(request)
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .build()
        )
        .build()

    val sdApi = retrofit.create(ApiService::class.java)
}

data class RequestBody(
    val prompt: String,
    val steps: Int,
    var negative_prompt:String = "Disfigured, blurry, nude"
//    var aspect_ratio:String = "1:1"
)