package com.balbugrahan.themovieapplication.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.balbugrahan.themovieapplication.model.Movie
import com.balbugrahan.themovieapplication.model.MovieContainer
import com.balbugrahan.themovieapplication.service.MovieApiService
import com.balbugrahan.themovieapplication.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainPageViewModel(application: Application) : BaseViewModel(application) {

    private val movieApiService = MovieApiService()
    private val disposable = CompositeDisposable()

    val movies = MutableLiveData<ArrayList<Movie>>()
    val movieError = MutableLiveData<Boolean>()
    val movieLoading = MutableLiveData<Boolean>()

    val movieList = ArrayList<Movie>()
    var pageNumber = 0

    fun refreshFromAPI() {
        pageNumber = 0
        movieList.clear()
        getDataFromAPI()
    }
    fun getDataFromAPI() {
        movieLoading.value = true
        disposable.add(
            movieApiService.getData(++pageNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieContainer>() {
                    override fun onSuccess(t: MovieContainer) {
                        t.movieList?.let { movieList.addAll(it) }
                        showMovies()
                    }

                    override fun onError(e: Throwable) {
                        movieLoading.value = false
                        movieError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }
    private fun showMovies() {
        movies.postValue(movieList)
        movieError.postValue(false)
        movieLoading.postValue(false)
    }
}