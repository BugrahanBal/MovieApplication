package com.balbugrahan.themovieapplication.ui.main

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balbugrahan.themovieapplication.R
import com.balbugrahan.themovieapplication.model.Movie
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

abstract class MainPageAdapter(var context: Context, movieList: ArrayList<Movie>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        var MOVIE_ROW = 20978
        var LOAD_MORE_ROW = 21
    }

    var unfilteredList = ArrayList<Movie>()
    var filteredList = ArrayList<Movie>()

    abstract fun onMovieClicked(movie: Movie, position: Int)
    abstract fun onLoadMoreClicked()

    init {
        unfilteredList.addAll(movieList)
        filteredList.addAll(movieList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MOVIE_ROW) {
            return MovieViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_movie,
                    parent,
                    false
                )
            )
        } else {
            return LoadMoreViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_load_more,
                    parent,
                    false
                )
            )
        }
    }
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (holder is MovieViewHolder) {

            val movieItem = filteredList[position]
            with(holder) {
                movieName.text = movieItem.title
                if (!TextUtils.isEmpty(movieItem.poster_path)) {
                    Picasso.get().load(Uri.parse(movieItem.getPosterFullPath()))
                        .into(movieImage)
                }
                itemView.setOnClickListener {
                    onMovieClicked(movieItem, position)
                }
            }
        } else if (holder is LoadMoreViewHolder) {

            with(holder) {
                loadMoreButton.setOnClickListener {
                    onLoadMoreClicked()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (filteredList.size == position) LOAD_MORE_ROW else MOVIE_ROW
    }

    override fun getItemCount(): Int {
        return filteredList.size + 1
    }

    fun filterMovies(filterMovie: String) {
        var filterMovie = filterMovie
        filteredList.clear()
        if (filterMovie.count() < 3) {
            filteredList.addAll(unfilteredList)
        } else {
            filterMovie = filterMovie.toLowerCase()
            for (item in unfilteredList) {
                if (item.title!!.toLowerCase(Locale.ROOT).contains(filterMovie) ||
                    item.original_title!!.toLowerCase(Locale.ROOT).contains(filterMovie)
                ) {
                    filteredList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var movieName: TextView = itemLayoutView.findViewById(R.id.movieName)
        var movieImage: ImageView = itemLayoutView.findViewById(R.id.movieImageView)
    }

    class LoadMoreViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var loadMoreButton: Button = itemLayoutView.findViewById(R.id.btn_loadmore)
    }
}