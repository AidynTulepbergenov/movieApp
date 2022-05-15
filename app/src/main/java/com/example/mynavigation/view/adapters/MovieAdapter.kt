package com.example.mynavigation.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynavigation.R
import com.example.mynavigation.databinding.MovieItemBinding
import com.example.mynavigation.model.data.Movie

class MovieAdapter(val itemClickListener: RecyclerViewItemClick? = null, val markFavItemClick: RecyclerViewFavItemClick? = null) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding: MovieItemBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.movie_item,
                parent,
                false
            )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.initContent(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size


    //movie view holder to bind items with recycler view
    inner class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initContent(data: Movie?) {
            binding.data = data
            binding.recyclerViewFavItemClick = markFavItemClick
            binding.recyclerViewItemClick = itemClickListener
            binding.executePendingBindings()
        }
    }

    interface RecyclerViewFavItemClick{
        fun itemClick(item: Movie)
    }

    interface RecyclerViewItemClick {
        fun itemClick(item: Movie)
    }

    //DiffUtil
    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.post_id == newItem.post_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Movie>?) {
        differ.submitList(list)
    }

}
