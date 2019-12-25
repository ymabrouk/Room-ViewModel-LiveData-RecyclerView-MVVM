package com.mindset.takingnote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mindset.takingnote.NoteAdapter.NoteHolder


class NoteAdapter : ListAdapter<Note, NoteHolder?>(DIFF_CALLBACK) {
    private var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote: Note = getItem(position)
        holder.textViewTitle.text = currentNote.getTitle()
        holder.textViewDescription.text = currentNote.getDescription()
        holder.textViewPriority.text = currentNote.getPriority().toString()
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    inner class NoteHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView
        val textViewDescription: TextView
        val textViewPriority: TextView

        init {
            textViewTitle = itemView.findViewById(R.id.title_tv)
            textViewDescription = itemView.findViewById(R.id.description_tv)
            textViewPriority = itemView.findViewById(R.id.priority_tv)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> =
            object : DiffUtil.ItemCallback<Note>() {
                override fun areItemsTheSame(
                    oldItem: Note,
                    newItem: Note
                ): Boolean {
                    return oldItem.getId() == newItem.getId()
                }

                override fun areContentsTheSame(
                    oldItem: Note,
                    newItem: Note
                ): Boolean {
                    return oldItem.getTitle() == newItem.getTitle()
                            && oldItem.getDescription() == newItem.getDescription()
                            && oldItem.getPriority() == newItem.getPriority()
                }
            }
    }
}