package com.example.usertask.adapter


import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.usertask.R
import com.example.usertask.databinding.ItemMovieBinding
import com.example.usertask.model.Movie


class MovieAdapter(private val onClick: (Movie) -> Unit) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    private val expandedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie, expandedItems.contains(position))
        }
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        init {
//            binding.tvOverview.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    toggleDescriptionExpansion(position)
//                }
//            }
//
//            binding.tvOverview.setCompoundDrawablesWithIntrinsicBounds(
//                0, 0, R.drawable.baseline_expand_more_24, 0
//            )
//            binding.tvOverview.compoundDrawablePadding = 8
//        }

        fun bind(movie: Movie, isExpanded: Boolean) {
            binding.apply {
                tvTitle.text = movie.title
                tvReleaseDate.text = "📅 ${movie.releaseDate}"
                tvOverview.text = movie.overview
                tvOverview.maxLines = if (isExpanded) Integer.MAX_VALUE else 2
//                tvOverview.compoundDrawables[2]?.let { drawable ->
//                    drawable.setLevel(if (isExpanded) 1 else 0)
//                    ObjectAnimator.ofFloat(
//                        drawable,
//                        "level",
//                        if (isExpanded) 0f else 1f,
//                        if (isExpanded) 1f else 0f
//                    ).apply {
//                        duration = 200
//                        start()
//                    }
//                }
                Glide.with(root.context)
                    .load(movie.getPosterUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivPoster)
                root.setOnClickListener { onClick(movie) }
            }
        }
    }

    private fun toggleDescriptionExpansion(position: Int) {
        if (expandedItems.contains(position)) {
            expandedItems.remove(position)
        } else {
            expandedItems.add(position)
        }
        notifyItemChanged(position)
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
}
