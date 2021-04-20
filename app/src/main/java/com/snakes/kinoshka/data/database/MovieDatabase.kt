package com.snakes.kinoshka.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MovieTypeConverter::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun moviesDao(): MovieDao
}