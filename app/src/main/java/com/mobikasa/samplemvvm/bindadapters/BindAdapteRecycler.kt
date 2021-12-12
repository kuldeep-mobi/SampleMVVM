package com.mobikasa.samplemvvm.bindadapters

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobikasa.samplemvvm.R

object BindAdapteRecycler {
    @JvmStatic
    @BindingAdapter("adapter")
    fun updateRemainingRecycler(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("image")
    fun setImageDrawable(imageView: ImageView, isFav: Boolean) {
        if (isFav) {
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    imageView.context,
                    R.drawable.fav_yes
                )
            )
        } else
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    imageView.context,
                    R.drawable.fav_no
                )
            )
    }
}