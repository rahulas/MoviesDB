package com.rahulas.movies.data.remote

import com.rahulas.movies.data.remote.model.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiService {

    @GET("movie/now_playing")
    fun loadPopularMovies(): Call<MoviesResponse>
}