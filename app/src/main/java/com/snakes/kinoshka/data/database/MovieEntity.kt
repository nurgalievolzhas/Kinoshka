package com.snakes.kinoshka.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snakes.kinoshka.MovieResponse
import com.snakes.kinoshka.util.Constants.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
class MovieEntity(
    var movieResponse: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id:Int = 0
}