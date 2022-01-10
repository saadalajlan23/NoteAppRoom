package com.example.noteapproom

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.note_row.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val noteDao by lazy { NoteDatabase.getDatabase(this).noteDao() }
    private lateinit var notes: List<Note>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notes = listOf()
        submit.setOnClickListener {
            addNote(note.text.toString())
            updateRV()
        }
        getItemsList()

        updateRV()
    }

    private fun updateRV(){
        noteRecyclerView.adapter = NoteAdapter(this, notes)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getItemsList(){
        CoroutineScope(IO).launch {
            val data = async {
                NoteRepository(noteDao).getNotes
            }.await()
            if(data.isNotEmpty()){
                notes = data
                Log.e("MainActivity", "$notes")
                Log.e("MainActivity", "$data")
                updateRV()
            }else{
                Log.e("MainActivity", "Unable to get data", )
            }
        }
    }


    private fun addNote(noteText: String){
        if(noteET.text.isEmpty()){
            Toast.makeText(this, "Error note is empty!!", Toast.LENGTH_LONG).show()
        }else{
            CoroutineScope(IO).launch {
                NoteRepository(noteDao).addNote(Note(0, noteText))
            }
            noteET.text.clear()
            Toast.makeText(this, "Note Added ", Toast.LENGTH_LONG).show()

        }

    }
    private fun editNote(noteID: Int, noteText: String){
        Toast.makeText(this, "Note edited", Toast.LENGTH_LONG).show()
        CoroutineScope(IO).launch {
            NoteRepository(noteDao).updateNote(Note(noteID,noteText))
        }
    }

    fun deleteNote(noteID: Int){
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_LONG).show()
        CoroutineScope(IO).launch {
            NoteRepository(noteDao).deleteNote(Note(noteID,""))


        }
    }

    fun raiseDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new text"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {

                    _, _ ->
                run {
                    editNote(id, updatedNote.text.toString())
                    updateRV()
                }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }
    fun deleteDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val textNote = TextView(this)
        textNote.text = "Delete this Note ?"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("DELETE", DialogInterface.OnClickListener {
                    _, _ -> deleteNote(id, )
            })
            .setNegativeButton("CANCEL", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Delete")
        alert.setView(textNote)
        alert.show()
    }
}