package com.laba.mydiary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntriesAdapter(
    private var entries: List<Entry>,
    private val userId: Long,
    private var context: Context
) :
    RecyclerView.Adapter<EntriesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_in_list)
        val date: TextView = view.findViewById(R.id.date_in_list)
        val btn: Button = view.findViewById(R.id.button_in_entry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.entry_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return entries.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = entries[position].title
        holder.date.text = entries[position].date
        holder.btn.setOnClickListener {
            val intent = Intent(context, EntryActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("entryId", entries[position].id)
            intent.putExtra("entryTitle", entries[position].title)
            intent.putExtra("entryText", entries[position].text)
            intent.putStringArrayListExtra("entryImages", entries[position].images)
            context.startActivity(intent)
        }
    }
}