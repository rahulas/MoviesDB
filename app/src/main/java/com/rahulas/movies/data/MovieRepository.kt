package com.rahulas.movies.data

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.rahulas.movies.app.MyApp
import com.rahulas.movies.data.local.MovieRoomDataBase
import com.rahulas.movies.data.local.dao.MovieDao
import com.rahulas.movies.data.local.dao.entity.MovieEntity
import com.rahulas.movies.data.network.NetworkBoundResource
import com.rahulas.movies.data.network.Resource
import com.rahulas.movies.data.remote.ApiConstant
import com.rahulas.movies.data.remote.MovieApiService
import com.rahulas.movies.data.remote.RequestInterceptor
import com.rahulas.movies.data.remote.model.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository {

    private var movieApiService: MovieApiService
    private var movieDao: MovieDao

    init {
        //Local from Room
        var movieRoomDataBase: MovieRoomDataBase = Room.databaseBuilder(
            MyApp.instance,
            MovieRoomDataBase::class.java,
            "db_movies"
        ).build()

        //Include API_KEY in query
        val okHttpClientBuilder: OkHttpClient.Builder =
            OkHttpClient.Builder().addInterceptor(RequestInterceptor())
        val client: OkHttpClient = okHttpClientBuilder.build()
        movieDao = movieRoomDataBase.getMovieDao()

        //REMOTE -> RETROFIT
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        movieApiService = retrofit.create(MovieApiService::class.java)
    }

    fun getFavMovies(): LiveData<List<MovieEntity>> {
        return movieDao.getFavMovies()
    }

    fun getNowPlayingMovies(): LiveData<Resource<List<MovieEntity>>> {
        return object : NetworkBoundResource<List<MovieEntity>, MoviesResponse>() {
            //Type return by room, type return by Retrofit
            override fun saveCallResult(@NonNull item: MoviesResponse) {
                movieDao.saveMovies(item.results)
            }

            override fun loadFromDb(): LiveData<List<MovieEntity>> {
                return movieDao.loadMovies()
            }

            override fun createCall(): Call<MoviesResponse> {
                return movieApiService.loadPopularMovies()
            }

        }.asLiveData()

    }

    fun updateMovieFavStatus(movie: MovieEntity) {
        GlobalScope.launch(Dispatchers.Default) {
            movieDao.updateMovieFavStatus(movie.id, movie.favourite)
        }
    }
}

