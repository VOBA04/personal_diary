package com.laba.mydiary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntriesAdapter(private var entries: List<Entry>, private var context: Context) : RecyclerView.Adapter<EntriesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title_in_list)
        val date = view.findViewById<TextView>(R.id.date_in_list)
        val btn = view.findViewById<Button>(R.id.button_in_entry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.entry_in_list, parent, false)
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
            intent.putExtra("entryId", entries[position].id)
            intent.putExtra("entryTitle", entries[position].title)
            intent.putExtra("entryText", entries[position].text)
            intent.putStringArrayListExtra("entryImages", entries[position].images)
            context.startActivity(intent)
        }
    }
}