package com.ddona.musicapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddona.musicapplication.databinding.ItemMusicOfflineBinding
import com.ddona.musicapplication.model.MusicOffline

class MusicOfflineAdapter(val inter: IMusic) : RecyclerView.Adapter<MusicOfflineAdapter.MusicOfflineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicOfflineViewHolder {
        return MusicOfflineViewHolder(
            ItemMusicOfflineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), inter
        )
    }

    override fun getItemCount() = inter.getCount()

    override fun onBindViewHolder(holder: MusicOfflineViewHolder, position: Int) {
        holder.binding.data = inter.getData(position)
    }



    interface IMusic {
        fun getCount(): Int
        fun getData(position: Int): MusicOffline
        fun onClick(position: Int)
    }

    class MusicOfflineViewHolder(val binding: ItemMusicOfflineBinding, inter: IMusic) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ll.setOnClickListener {
                inter.onClick(adapterPosition)
            }
        }
    }
}