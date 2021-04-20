package com.snakes.kinoshka.data.network

import com.snakes.kinoshka.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @QueryMap queries:Map<String,String>
    ):Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @QueryMap searchQuery: Map<String, String>
    ): Response<MovieResponse>
}