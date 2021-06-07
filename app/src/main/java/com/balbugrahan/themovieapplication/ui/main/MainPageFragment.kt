package com.balbugrahan.themovieapplication.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balbugrahan.themovieapplication.R
import com.balbugrahan.themovieapplication.model.Movie
import kotlinx.android.synthetic.main.fragment_main.*

class MainPageFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainPageViewModel
    private lateinit var mainPageAdapter: MainPageAdapter
    private var layoutManager : GridLayoutManager? = null
    var isListFiltered = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainPageViewModel::class.java)

        layoutManager = GridLayoutManager(context, 2)
        layoutManager!!.orientation = LinearLayoutManager.VERTICAL
        layoutManager!!.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mainPageAdapter.getItemViewType(position)) {
                    MainPageAdapter.MOVIE_ROW -> 1
                    MainPageAdapter.LOAD_MORE_ROW -> 2
                    else -> -1
                }
            }
        }
        rvMovieList.layoutManager = layoutManager

        swipeRefreshLayout.setOnRefreshListener {
            rvMovieList.visibility = View.GONE
            movieError.visibility = View.GONE
            movieLoading.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
        viewModel.getDataFromAPI()
        setupSearchView()
    }

    private fun setupSearchView() {
        val searchManager: SearchManager =
            context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.setOnQueryTextListener(this@MainPageFragment)
    }

    private fun observeLiveData() {
        viewModel.movies.observe(viewLifecycleOwner, { movies ->

            val index = layoutManager!!.findFirstVisibleItemPosition()
            val v = rvMovieList.getChildAt(0)
            val top = if (v == null) 0 else v.top - rvMovieList.paddingTop

            movies?.let {
                setAdapter(movies)
            }

            layoutManager!!.scrollToPositionWithOffset(index, top)
        })
        viewModel.movieError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    movieError.visibility = View.VISIBLE
                } else {
                    movieError.visibility = View.GONE
                }
            }
        })
        viewModel.movieLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    movieLoading.visibility = View.VISIBLE
                    movieError.visibility = View.GONE
                } else {
                    movieLoading.visibility = View.GONE
                }
            }
        })
    }

    private fun setAdapter(movies: ArrayList<Movie>) {
        rvMovieList.visibility = View.VISIBLE

        mainPageAdapter = object : MainPageAdapter(requireContext(), movies) {
            override fun onMovieClicked(movie: Movie, position: Int) {
                movie.let {
                    val action = MainPageFragmentDirections.actionMainFragmentToDetailsFragment(movie)
                    Navigation.findNavController(view!!).navigate(action)
                }
            }

            override fun onLoadMoreClicked() {
                viewModel.getDataFromAPI()
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            }
        }
        rvMovieList.adapter = mainPageAdapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(filterText: String?): Boolean {
        isListFiltered = (filterText?.count() ?: 0) > 2
        mainPageAdapter.filterMovies(filterText!!)
        return true
    }

}