package com.example.noteapproom

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_row.view.*

class NoteAdapter(
    private val activity: MainActivity,
    private val items: List<Note>
): RecyclerView.Adapter<NoteAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.note_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteAdapter.ItemViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            note.text = item.noteText
            if(position%2==0){
                noteRow.setBackgroundColor(Color.LTGRAY)}
            edit.setOnClickListener {
                activity.raiseDialog(item.id)
            }
            delete.setOnClickListener {
                activity.deleteDialog(item.id)
            }
        }
    }

    override fun getItemCount() = items.size
}