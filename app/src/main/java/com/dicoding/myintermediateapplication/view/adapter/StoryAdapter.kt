package com.dicoding.myintermediateapplication.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myintermediateapplication.data.response.ListStoryItem
import com.dicoding.myintermediateapplication.databinding.ListItemRowBinding
import com.dicoding.myintermediateapplication.view.detail.DetailActivity
import com.dicoding.myintermediateapplication.view.main.MainActivity

class StoryAdapter:
    ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener{
//            val sendData= Intent(holder.itemView.context, DetailActivity::class.java)
//            sendData.putExtra(DetailActivity.USER,user.id)
//            sendData.putExtra(DetailActivity.NAME,user.name)
//            holder.itemView.context.startActivity(sendData)
     }
    }

    class MyViewHolder(val binding: ListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListStoryItem) {
            val img = review.photoUrl
            Glide.with(binding.root.context)
                .load(img)
                .into(binding.itemPhoto)
            binding.itemName.text = "${review.name}"
            binding.itemDescription.text = review.description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}