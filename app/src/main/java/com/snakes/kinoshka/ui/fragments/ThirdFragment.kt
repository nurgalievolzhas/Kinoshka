package com.snakes.kinoshka.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.snakes.kinoshka.R
import com.snakes.kinoshka.Result
import com.snakes.kinoshka.util.Constants.BUNDLE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_third.view.*


class ThirdFragment : Fragment() {
    //Views
    private lateinit var mView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView  =  inflater.inflate(R.layout.fragment_third, container, false)

        val myBundle:Result? = arguments?.getParcelable(BUNDLE_RESULT_KEY)

        mView.movie_wv.webViewClient = object : WebViewClient(){}


        return mView
    }

}