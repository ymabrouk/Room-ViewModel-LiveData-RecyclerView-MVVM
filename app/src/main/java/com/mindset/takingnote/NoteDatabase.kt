package com.mindset.takingnote

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao


    companion object {
        private var instance: NoteDatabase? = null
        @Synchronized
        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java, "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return instance
        }

         val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance).execute()
            }
        }
        class PopulateDbAsyncTask  constructor(db: NoteDatabase?) :
            AsyncTask<Void?, Void?, Void?>() {
            private val noteDao: NoteDao
             override fun doInBackground(vararg params: Void?): Void? {
                noteDao.insert(Note("Title 1", "Description 1", 1))
                noteDao.insert(Note("Title 2", "Description 2", 2))
                noteDao.insert(Note("Title 3", "Description 3", 3))
                return null
            }

            init {
                noteDao = db!!.noteDao()
            }
        }
    }
}