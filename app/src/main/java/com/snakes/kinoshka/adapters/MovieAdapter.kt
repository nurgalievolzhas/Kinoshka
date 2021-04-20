package com.snakes.kinoshka.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.snakes.kinoshka.MovieResponse
import com.snakes.kinoshka.Result
import com.snakes.kinoshka.databinding.ItemMovieBinding
import com.snakes.kinoshka.util.MovieDiffUtil

class MovieAdapter:RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    //DATA
    private var movies = emptyList<Result>()

    class MovieViewHolder(private val binding:ItemMovieBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(movie:Result){
            binding.item = movie
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):MovieViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieBinding.inflate(layoutInflater,parent,false)
                return MovieViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movies[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newData:MovieResponse){
        val movieDiffUtil = MovieDiffUtil(movies, newData.results!!)
        val diffUtilResults = DiffUtil.calculateDiff(movieDiffUtil)
        movies = newData.results
        diffUtilResults.dispatchUpdatesTo(this)
        //notifyDataSetChanged()
    }
}