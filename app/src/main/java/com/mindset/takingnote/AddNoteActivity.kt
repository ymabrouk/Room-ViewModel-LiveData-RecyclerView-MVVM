package com.mindset.takingnote

import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddNoteActivity : AppCompatActivity() {


    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var numberPickerPriority : NumberPicker

    companion object{

        const val EXTRA_TITLE = "com.codinginflow.architectureexample.EXTRA_TITLE"
        const val EXTRA_ID = "com.codinginflow.architectureexample.EXTRA_ID"
        const val EXTRA_DESCRIPTION = "com.codinginflow.architectureexample.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "com.codinginflow.architectureexample.EXTRA_PRIORITY"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)


        titleEditText = findViewById(R.id.editText_title)
        descriptionEditText = findViewById(R.id.editText_description)
        numberPickerPriority = findViewById(R.id.number_picker_priority)


        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            titleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            descriptionEditText.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Note");
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_menu -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun discardNote() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun saveNote() {
        val title: String = titleEditText.getText().toString()
        val description: String = descriptionEditText.getText().toString()
        val priority = numberPickerPriority.value

        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }



        val intent = Intent()
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_PRIORITY, priority);


        val id = getIntent().getIntExtra(NfcAdapter.EXTRA_ID, -1)
        if (id != -1) {
            intent.putExtra(NfcAdapter.EXTRA_ID, id)
        }
        setResult(Activity.RESULT_OK, intent)


        finish()

    }

}


