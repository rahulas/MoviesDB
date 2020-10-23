package com.rahulas.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahulas.movies.data.local.dao.MovieDao
import com.rahulas.movies.data.local.dao.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieRoomDataBase: RoomDatabase() {

    abstract fun getMovieDao(): MovieDao
}