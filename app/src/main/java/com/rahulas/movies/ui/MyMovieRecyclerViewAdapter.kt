package com.rahulas.movies.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahulas.movies.R
import com.rahulas.movies.data.local.dao.entity.MovieEntity
import com.rahulas.movies.data.remote.ApiConstant
import kotlinx.android.synthetic.main.fragment_movie.view.*

class MyMovieRecyclerViewAdapter(
    private val context: Context,
    private var mValues: List<MovieEntity>, private val listener: MovieItemListener
) : RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        Glide.with(context)
            .load(ApiConstant.IMAGE_API_PREFIX + item.poster_path)
            .into(holder.imageViewCover)
        holder.movieTitle.text = item.title

        if (item.favourite)
            holder.favButton.setBackgroundResource(R.drawable.ic_heart_red)
        else
            holder.favButton.setBackgroundResource(R.drawable.ic_heart_white)

        holder.favButton.setOnClickListener {
            if (item.favourite)
                holder.favButton.setBackgroundResource(R.drawable.ic_heart_white)
            else
                holder.favButton.setBackgroundResource(R.drawable.ic_heart_red)

            item.favourite = !item.favourite

            listener.onClickedFavourite(item)
        }
    }

    fun setData(movies: List<MovieEntity>) {
        mValues = movies
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (mValues.size !== 0) {
            return mValues.size
        } else {
            return 0
        }
    }

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        val imageViewCover: ImageView = mView.imageViewCover
        val movieTitle: AppCompatTextView = mView.movieTitle
        val favButton: AppCompatButton = mView.favButton

    }

    interface MovieItemListener {
        fun onClickedFavourite(movie: MovieEntity)
    }
}
