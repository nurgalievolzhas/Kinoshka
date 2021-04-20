package com.snakes.kinoshka.bindingadapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.snakes.kinoshka.MovieResponse
import com.snakes.kinoshka.data.database.MovieEntity
import com.snakes.kinoshka.util.NetworkResult

class FragmentMoviesBinding {

    companion object{

        @BindingAdapter("readApiResponse","readDatabase",requireAll = true)
        @JvmStatic
        fun errorViewsVisibility(
            view: View,
            apiResponse:NetworkResult<MovieResponse>?,
            database:List<MovieEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                view.visibility = View.VISIBLE
                if (view is TextView){
                    view.text = apiResponse.message.toString()
                }
            }else if (apiResponse is NetworkResult.Loading){
                view.visibility = View.INVISIBLE
            }
            else if (apiResponse is NetworkResult.Success){
                view.visibility = View.INVISIBLE
            }
        }
    }
}