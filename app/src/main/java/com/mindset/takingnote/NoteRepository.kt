package com.mindset.takingnote

import android.app.Application
import android.os.AsyncTask

import androidx.lifecycle.LiveData


class NoteRepository() {
    private var noteDao: NoteDao? = null
    private var allNotes: LiveData<List<Note>>? = null



    constructor(application: Application) : this(){
        val database: NoteDatabase? = NoteDatabase.getInstance(application.applicationContext)
        this.noteDao = database?.noteDao()
        allNotes = noteDao?.getAllNotes()
    }



    fun insert(note: Note?) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note?) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note?) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(noteDao).execute()
    }

    fun getAllNotes(): LiveData<List<Note>>? {
        return allNotes
    }

    private class InsertNoteAsyncTask() : AsyncTask<Note, Void, Void>() {

        private var noteDao: NoteDao? = null


        constructor(noteDao: NoteDao?) : this(){
            this.noteDao =  noteDao
        }


        override fun doInBackground(vararg notes: Note): Void? {
            noteDao?.insert(notes[0])
            return null;
        }
    }


    private class UpdateNoteAsyncTask() : AsyncTask<Note, Void, Void>() {

        private var noteDao: NoteDao? = null


        constructor(noteDao: NoteDao?) : this(){
            this.noteDao =  noteDao
        }


        override fun doInBackground(vararg notes: Note): Void? {
            noteDao?.update(notes[0])
            return null;
        }
    }


    private class DeleteNoteAsyncTask() : AsyncTask<Note, Void, Void>() {

        private var noteDao: NoteDao? = null


        constructor(noteDao: NoteDao?) : this(){
            this.noteDao =  noteDao
        }


        override fun doInBackground(vararg notes: Note): Void? {
            noteDao?.delete(notes[0])
            return null;
        }
    }


    private class DeleteAllNotesAsyncTask() : AsyncTask<Note, Void, Void>() {

        private var noteDao: NoteDao? = null


        constructor(noteDao: NoteDao?) : this(){
            this.noteDao =  noteDao
        }


        override fun doInBackground(vararg notes: Note): Void? {
            noteDao?.deleteAllNotes()
            return null;
        }
    }


}