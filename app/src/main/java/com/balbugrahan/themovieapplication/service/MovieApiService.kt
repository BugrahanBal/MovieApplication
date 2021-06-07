package com.balbugrahan.themovieapplication.service

import android.util.Log
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.balbugrahan.themovieapplication.model.MovieContainer
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MovieApiService {

    private val BASE_URL = "https://api.themoviedb.org/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(provideOkHttp())
        .build()
        .create(MovieApi::class.java)

    private fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor {
            Log.e("OkHttp", it)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequestBuilder = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .cacheControl(CacheControl.FORCE_NETWORK)
            return@addInterceptor chain.proceed(newRequestBuilder.build())
        }
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    fun getData(pageNumber : Int): Single<MovieContainer> {
        return api.getMovies(pageNumber)
    }

}