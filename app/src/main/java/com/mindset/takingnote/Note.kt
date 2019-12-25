package com.mindset.takingnote

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
class Note(title: String, description: String, priority: Int) {
    @PrimaryKey(autoGenerate = true)
    private var id = 0
    private val title: String
    private val description: String
    private val priority: Int
    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return id
    }

    fun getTitle(): String {
        return title
    }

    fun getDescription(): String {
        return description
    }

    fun getPriority(): Int {
        return priority
    }

    init {
        this.title = title
        this.description = description
        this.priority = priority
    }
}