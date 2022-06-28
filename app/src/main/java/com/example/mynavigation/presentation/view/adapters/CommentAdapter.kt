package com.example.mynavigation.presentation.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynavigation.R
import com.example.mynavigation.databinding.CommentItemBinding
import com.example.mynavigation.domain.model.responses.Comment

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding: CommentItemBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.comment_item,
                parent,
                false
            )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.initContent(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CommentViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initContent(data: Comment?) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Comment>?) {
        differ.submitList(list)
    }
}