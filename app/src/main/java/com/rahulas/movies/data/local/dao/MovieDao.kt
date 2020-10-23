package com.rahulas.movies.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rahulas.movies.data.local.dao.entity.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun loadMovies(): LiveData<List<MovieEntity>>

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(movieEntityList: List<MovieEntity>)*/

    fun saveMovies(movieEntityList: List<MovieEntity>) {
        for (movie in movieEntityList) {
            saveMovie(movie)
        }
    }

    @Transaction
    fun saveMovie(movie: MovieEntity) {
        if (getMovie(movie.id) != null) {
            movie.favourite = getMovieFavStatus(movie.id)
            updateMovie(movie)
        } else {
            addMovies(movie)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovies(movie: MovieEntity)

    @Update
    fun updateMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovie(movieId: Int): MovieEntity

    @Query("UPDATE movies SET favourite = :fav WHERE id = :movieId")
    suspend fun updateMovieFavStatus(movieId: Int, fav: Boolean)

    @Query("SELECT favourite FROM movies WHERE id = :movieId")
    fun getMovieFavStatus(movieId: Int): Boolean

    @Query("SELECT * FROM movies WHERE favourite = :fav")
    fun getFavMovies(fav: Boolean): LiveData<List<MovieEntity>>

    fun getFavMovies(): LiveData<List<MovieEntity>> {
        return getFavMovies(true)
    }


}