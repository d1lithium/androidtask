package com.verodigital.androidtask.data.datasource.remote.api



import com.google.gson.GsonBuilder
import com.verodigital.androidtask.data.datasource.Task
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface LoginService {

    @GET("v1/tasks/select")
    suspend fun getAllTasks(
       @Header("Authorization") authorization:  String

    ): List<Task>

    @POST("login/")
    suspend fun login(
      @Body credentials: HashMap<String, String>

    ): LoginResponse


    companion object {

        private const val BASE_URL: String  = "https://api.baubuddy.de/index.php/";

        fun create(): LoginService{
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val mediaType = "application/json".toMediaTypeOrNull()
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(HeaderInterceptor())
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

           return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(LoginService::class.java)




        }




    }

}