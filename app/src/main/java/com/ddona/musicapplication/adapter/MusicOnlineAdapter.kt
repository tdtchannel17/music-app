package com.ddona.musicapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddona.musicapplication.databinding.ItemMusicOnlineBinding
import com.ddona.musicapplication.model.MusicOnline

class MusicOnlineAdapter(val inter:IMusicOnline) :
    RecyclerView.Adapter<MusicOnlineAdapter.MusicOnlineHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicOnlineHolder {
        return MusicOnlineHolder(
            ItemMusicOnlineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ),inter
        )
    }

    override fun getItemCount()=inter.getCount()

    override fun onBindViewHolder(holder: MusicOnlineHolder, position: Int) {
        holder.binding.data = inter.getData(position);
    }

    interface IMusicOnline{
        fun onItemClick(position:Int)
        fun getCount():Int
        fun getData(position:Int):MusicOnline
    }
    class MusicOnlineHolder(val binding:ItemMusicOnlineBinding,
                             inter: IMusicOnline) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener({
                inter.onItemClick(adapterPosition)
            })
        }
    }
}