package com.snakes.kinoshka.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.snakes.kinoshka.MovieResponse

class MovieTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun movieResponseToString(movieResponse: MovieResponse):String{
        return gson.toJson(movieResponse)
    }

    @TypeConverter
    fun stringToMovieResponse(data:String): MovieResponse {
        val listType = object : TypeToken<MovieResponse>(){}.type
        return gson.fromJson(data,listType)
    }
}