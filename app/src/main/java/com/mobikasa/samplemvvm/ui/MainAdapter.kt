package com.mobikasa.samplemvvm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobikasa.samplemvvm.data.Results
import com.mobikasa.samplemvvm.databinding.ItemRecyclerBinding

class MainAdapter(var mList: List<Results>?, var onSelect: (results: Results?) -> Unit) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    fun setData(list: List<Results>?) {
        this.mList = list
        mList?.let {
            notifyItemRangeInserted(0, it.size)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mList?.let {
            holder.bindData(it[position], onSelect)
        }
    }

    override fun getItemCount(): Int {
        val list = mList ?: ArrayList()
        return list.size
    }

    inner class ViewHolder(private var itemRecyclerBinding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(itemRecyclerBinding.root) {

        init {
            itemRecyclerBinding.imageView.setOnClickListener {
                val status = mList?.get(adapterPosition)?.isFavorite ?: false
                mList?.get(adapterPosition)?.isFavorite = !status
                notifyItemChanged(adapterPosition)
            }
        }

        fun bindData(item: Results?, onSelect: (results: Results?) -> Unit) {
            itemRecyclerBinding.result = item
            itemRecyclerBinding.root.setOnClickListener {
                onSelect.invoke(item)
            }
        }
    }
}