package id.infiniteuny.apps.data.network

import id.infiniteuny.apps.data.network.responses.AnnouncementResponse
import id.infiniteuny.apps.data.network.responses.NewsContentResponse
import id.infiniteuny.apps.data.network.responses.NewsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface NewsApi {
    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): NewsApi {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://uny-api.herokuapp.com/api/university/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }

    @GET("news/page/{page}")
    suspend fun getNews(@Path("page") page: Int): Response<NewsResponse>

    @GET("news/open")
    suspend fun getNewsContent(@Query("link") link: String): Response<NewsContentResponse>

    @GET("announcement/page/{page}")
    suspend fun getAnnouncement(@Path("page") page: Int): Response<AnnouncementResponse>
}