package com.snakes.kinoshka.bindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.snakes.kinoshka.R
import com.snakes.kinoshka.Result
import com.snakes.kinoshka.ui.fragments.movies.MoviesFragmentDirections
import java.lang.Exception

class ItemMovieBinding {

    companion object{

        @BindingAdapter("onMovieClickListener")
        @JvmStatic
        fun onMovieClickListener(itemMovieBinding:ConstraintLayout,result: Result){
            itemMovieBinding.setOnClickListener {
                try {
                    val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsActivity(result)
                    itemMovieBinding.findNavController().navigate(action)
                }catch (e:Exception){
                    Log.d("ItemMovieBinding",e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView,imageUrl:String){
            val imageUrl1 = "https://image.tmdb.org/t/p/w500/$imageUrl"
            imageView.load(imageUrl1){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("setDoubleToString")
        @JvmStatic
        fun setDoubleToString(textView: TextView,popularity:Double){
            textView.text = popularity.toString()
        }
    }
}