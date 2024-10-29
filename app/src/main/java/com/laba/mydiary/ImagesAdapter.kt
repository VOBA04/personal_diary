package com.laba.mydiary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class ImagesAdapter(
    private val images: ArrayList<String>,
    private val context: Context
) :
    RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {
    private lateinit var clickListener: OnItemClickListener

    class MyViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView_in_entry)

        init {
            view.setOnLongClickListener {
                listener.onLongItemClick(adapterPosition, view)
                true
            }
        }
    }

    fun onClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    interface OnItemClickListener {
        fun onLongItemClick(position: Int, view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image, parent, false)
        return MyViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        context.contentResolver.takePersistableUriPermission(
            images[position].toUri(),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        holder.imageView.setImageURI(images[position].toUri())

    }

    override fun getItemCount(): Int {
        return images.count()
    }

    fun addToAdapter(image: Uri) {
        images.add(image.toString())
    }

    fun removeFromAdapter(position: Int) {
        images.removeAt(position)
    }

}