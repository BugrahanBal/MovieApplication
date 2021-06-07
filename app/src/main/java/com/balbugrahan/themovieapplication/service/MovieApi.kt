package com.balbugrahan.themovieapplication.service

import io.reactivex.Single
import retrofit2.http.GET
import com.balbugrahan.themovieapplication.model.MovieContainer
import retrofit2.http.Query

internal interface MovieApi {

    @GET("3/movie/top_rated?api_key=11459cff1c1ce00e3202addab99f3a91&language=en-us")
    fun getMovies(@Query("page") pageNumber : Int): Single<MovieContainer>
}