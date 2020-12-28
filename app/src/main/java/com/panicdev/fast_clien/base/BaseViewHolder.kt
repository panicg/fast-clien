package com.panicdev.fast_clien.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in ITEM : Any>(
    val context: Context,
    @LayoutRes layoutRes: Int,
    parent: ViewGroup?)
    : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(layoutRes, parent, false)) {

    fun onBindViewHolder(item: Any?) {
        try {
            onViewCreated(item as? ITEM?)
        } catch (e: Exception) {
            itemView.visibility = View.GONE
        }
    }

    abstract fun onViewCreated(item: ITEM?)
}