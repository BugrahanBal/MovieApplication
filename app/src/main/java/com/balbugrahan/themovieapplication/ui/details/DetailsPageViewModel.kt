package com.balbugrahan.themovieapplication.ui.details

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.balbugrahan.themovieapplication.model.Movie
import com.balbugrahan.themovieapplication.ui.BaseViewModel
import kotlinx.coroutines.launch

class DetailsPageViewModel(application: Application) : BaseViewModel(application) {

    val movieLiveData = MutableLiveData<Movie>()

    fun getDataForDetails(movie: Movie) {
        launch {
            movieLiveData.value = movie
        }
    }
}