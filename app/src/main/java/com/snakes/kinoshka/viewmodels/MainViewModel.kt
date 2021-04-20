package com.snakes.kinoshka.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.Movie
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.snakes.kinoshka.MovieResponse
import com.snakes.kinoshka.data.Repository
import com.snakes.kinoshka.data.database.MovieEntity
import com.snakes.kinoshka.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /**ROOM DATABASE*/
    val readMovie:LiveData<List<MovieEntity>> = repository.local.readDatabase().asLiveData()
    fun insertMovies(movieEntity: MovieEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMovies(movieEntity)
        }
    }

    /**RETROFIT*/
    var movieResponse: MutableLiveData<NetworkResult<MovieResponse>> = MutableLiveData()
    var searchMovieResponse:MutableLiveData<NetworkResult<MovieResponse>> = MutableLiveData()
    fun getPopularMovies(queries:Map<String,String>){
        viewModelScope.launch { getPopularMoviesSafeCall(queries) }
    }

    fun getSearchMovies(searchQuery:Map<String,String>) = viewModelScope.launch {
        getSearchMoviesSafeCall(searchQuery)
    }

    private suspend fun getSearchMoviesSafeCall(searchQuery: Map<String, String>) {
        searchMovieResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.searchMovie(searchQuery)
                searchMovieResponse.value = handlePopularMoviesResponse(response)

            }catch (e:Exception){
                searchMovieResponse.value = NetworkResult.Error("Movies not found.")
            }
        }else{
            searchMovieResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getPopularMoviesSafeCall(queries: Map<String, String>) {
        movieResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getPopularMovies(queries)
                movieResponse.value = handlePopularMoviesResponse(response)

                val movie = movieResponse.value!!.data
                if (movie!=null){
                    offlineCacheMovies(movie)
                }
            }catch (e:Exception){
                movieResponse.value = NetworkResult.Error("Movies not found.")
            }
        }else{
            movieResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheMovies(movie: MovieResponse) {
        val movieEntity = MovieEntity(movie)
        insertMovies(movieEntity)
    }

    private fun handlePopularMoviesResponse(response: Response<MovieResponse>): NetworkResult<MovieResponse>? {
        when{
            response.message().toString().contains("timeout") ->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 ->{
                return NetworkResult.Error("Api Key limited.")
            }
            response.body()!!.results.isNullOrEmpty() ->{
                return NetworkResult.Error("Movies not found.")
            }
            response.isSuccessful ->{
                val movieResponse = response.body()
                return NetworkResult.Success(movieResponse!!)
            }
            else ->{
                return NetworkResult.Error(response.message().toString())
            }
        }
    }


    private fun hasInternetConnection():Boolean{
         val connectivityManager = getApplication<Application>().getSystemService(
             Context.CONNECTIVITY_SERVICE
         ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}