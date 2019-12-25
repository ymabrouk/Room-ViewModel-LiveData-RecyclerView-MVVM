package com.mindset.takingnote

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mindset.takingnote.NoteAdapter.OnItemClickListener


class MainActivity : AppCompatActivity() {

    private var noteViewModel: NoteViewModel? = null

    val ADD_NOTE_REQUEST = 1
    val EDIT_NOTE_REQUEST = 2;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAddNote: FloatingActionButton = findViewById(R.id.button_icon)
        buttonAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = NoteAdapter()


        adapter.setOnItemClickListener(object :
            OnItemClickListener {
            override fun onItemClick(note: Note?) {
                val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
                intent.putExtra(AddNoteActivity.EXTRA_ID, note!!.getId())
                intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle())
                intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription())
                intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority())
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })

        recyclerView.adapter = adapter


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel!!.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)



        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel!!.getAllNotes()!!.observe(
            this,
            Observer<List<Note?>?> {

                    notes -> adapter.submitList(notes)


            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK){

            val title: String = data?.getStringExtra(AddNoteActivity.EXTRA_TITLE)!!
            val description: String = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION)!!
            val priority: Int = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0)


            val addedNote: Note = Note(title, description, priority)
            noteViewModel?.insert(addedNote);
            Toast.makeText(this, "Note added successfully ", Toast.LENGTH_LONG).show()

        }else  if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {

            val title: String = data?.getStringExtra(AddNoteActivity.EXTRA_TITLE)!!
            val description: String = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION)!!
            val priority: Int = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0)

            val id: Int = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }


            val note: Note = Note(title, description, priority)
            note.setId(id)
            noteViewModel?.update(note);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

        } else {
                Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                deleteAllNotes()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllNotes() {

        noteViewModel?.deleteAllNotes()
        Toast.makeText(this, "All Note deleted successfully ", Toast.LENGTH_LONG).show()
    }
}
