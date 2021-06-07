package com.balbugrahan.themovieapplication.ui.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.balbugrahan.themovieapplication.R
import com.balbugrahan.themovieapplication.model.Movie
import com.balbugrahan.themovieapplication.ui.main.MainPageFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsPageFragment : Fragment() {

    private lateinit var viewModel: DetailsPageViewModel
    private lateinit var movie: Movie


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movie = DetailsPageFragmentArgs.fromBundle(it).movieModel
        }
        viewModel = ViewModelProviders.of(this).get(DetailsPageViewModel::class.java)
        observeLiveData()
        viewModel.getDataForDetails(movie)
        //Back Button
        backButton.setOnClickListener(View.OnClickListener {
            val action =DetailsPageFragmentDirections.actionDetailsFragmentToMainFragment()
            Navigation.findNavController(view).navigate(action)
        })
    }

    private fun observeLiveData() {
        viewModel.movieLiveData.observe(viewLifecycleOwner, { movie ->
            movie?.let {

                movieTitle.text = movie.title
                moviePopularity.text = "Popularity: " + movie.popularity.toString()
                voteAverage.text = "Vote Average:  " + movie.vote_average.toString()
                movieOverview.text = movie.overview
                Picasso.get().load(Uri.parse(movie.getPosterFullPath())).into(movieImageDetails)
            }
        })
    }



}