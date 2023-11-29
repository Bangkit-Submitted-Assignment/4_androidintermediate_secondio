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

class StoryAdapter :
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
        val story = getItem(position)
        holder.bind(story)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("STORY_ID", story.id) // Mengirim ID cerita ke DetailActivity
            context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            val img = story.photoUrl
            Glide.with(binding.root.context)
                .load(img)
                .into(binding.itemPhoto)
            binding.itemName.text = "${story.name}"
            binding.itemDescription.text = story.description
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
