package com.snakes.kinoshka.data

import android.graphics.Movie
import com.snakes.kinoshka.MovieResponse
import com.snakes.kinoshka.data.network.MovieApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getPopularMovies(queries:Map<String,String>):Response<MovieResponse>{
        return movieApi.getPopularMovies(queries)
    }

    suspend fun searchMovie(searchQuery:Map<String,String>):Response<MovieResponse>{
        return movieApi.searchMovie(searchQuery)
    }
}