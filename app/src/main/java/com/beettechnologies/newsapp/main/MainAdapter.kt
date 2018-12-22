package com.beettechnologies.newsapp.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.beettechnologies.newsapp.common.data.model.News
import com.beettechnologies.newsapp.databinding.ItemNewsBinding
import java.util.ArrayList

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var items: MutableList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    fun setItems(items: MutableList<News>) {
        this.items.clear()
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: News) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}