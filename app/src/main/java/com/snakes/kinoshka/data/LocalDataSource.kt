package com.snakes.kinoshka.data

import com.snakes.kinoshka.data.database.MovieDao
import com.snakes.kinoshka.data.database.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val movieDao: MovieDao
) {

    suspend fun insertMovies(movieEntity: MovieEntity){
        movieDao.insertMovie(movieEntity)
    }

    fun readDatabase():Flow<List<MovieEntity>>{
        return movieDao.readMovies()
    }
}