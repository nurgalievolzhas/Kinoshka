package com.snakes.kinoshka.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.snakes.kinoshka.R
import com.snakes.kinoshka.Result
import com.snakes.kinoshka.util.Constants
import com.snakes.kinoshka.util.Constants.BUNDLE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_first.view.*


class FirstFragment : Fragment() {
    //Views
    private lateinit var mView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_first, container, false)

        val myBundle:Result? = arguments?.getParcelable(BUNDLE_RESULT_KEY)
        val imageUrl = "https://image.tmdb.org/t/p/w500/" + myBundle?.posterPath
        mView.movie_poster_iv.load(imageUrl)
        mView.movie_title_tv.text = myBundle?.originalTitle
        mView.movie_lang_tv.text = myBundle?.originalLanguage
        mView.movie_popularity_tv.text = myBundle?.popularity.toString()
        mView.movie_overview_tv.text = myBundle?.overview

        return mView
    }


}