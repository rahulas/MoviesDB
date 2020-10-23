package com.rahulas.movies.data.remote.model

import com.rahulas.movies.data.local.dao.entity.MovieEntity

data class MoviesResponse(
    var results: List<MovieEntity>
)