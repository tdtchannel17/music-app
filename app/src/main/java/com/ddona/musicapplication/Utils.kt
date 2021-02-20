package com.ddona.musicapplication

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat

object Utils {
    @JvmStatic
    @BindingAdapter("setText")
    fun setText(tv: TextView, content: String?) {
        tv.setText(content)
    }
    @JvmStatic
    @BindingAdapter("setText")
    fun setText(tv: TextView, content: Int) {
        tv.setText(content.toString())
    }
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    @BindingAdapter("setDurationMusic")
    fun setDurationMusic(tv: TextView, duration: Long?) {
        if (duration == null){
            tv.setText("")
            return
        }
        val simple = SimpleDateFormat("mm:ss")
        tv.setText(simple.format(duration))
    }

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(iv: ImageView, imageId: Int) {
        iv.setImageResource(imageId)
    }
    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(iv: ImageView, path: String?) {
        if (path == null){
            return
        }
        Glide.with(iv.context)
            .load(File(path))
            .into(iv)
    }
    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(iv: ImageView, uri: Uri?) {
        if (uri == null){
            return
        }
        Glide.with(iv.context)
            .load(uri)
            .into(iv)
    }

    @JvmStatic
    @BindingAdapter("setImageLink")
    fun setImageLink(iv: ImageView, link: String) {
        Glide.with(iv.context)
            .load(link)
            .into(iv)
    }
}