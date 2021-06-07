package com.balbugrahan.themovieapplication.model

import com.google.gson.annotations.SerializedName

data class MovieContainer(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val movieList: ArrayList<Movie>? = null)