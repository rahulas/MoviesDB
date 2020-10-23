package com.rahulas.movies.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rahulas.movies.data.MovieRepository
import com.rahulas.movies.data.local.dao.entity.MovieEntity
import com.rahulas.movies.data.network.Resource

class MovieViewModel : ViewModel() {

    companion object {
        private var movieRepository = MovieRepository()
        var popularMovies: LiveData<Resource<List<MovieEntity>>> =
            movieRepository.getNowPlayingMovies()
    }

    fun getPopularMovies(): LiveData<Resource<List<MovieEntity>>> {
        return popularMovies
    }

    fun getFavMovies(): LiveData<List<MovieEntity>> {
        return movieRepository.getFavMovies()
    }

    fun updateMovieFavStatus(movie: MovieEntity) {
        movieRepository.updateMovieFavStatus(movie)
    }
}