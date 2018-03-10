package com.example.anna.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // get Access to Firebase database, no need of any URL, Firebase identifies the connection via
    // the package name of the app
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define floating action button
        val floatingActionButton = this.findViewById<View>(R.id.floatingActionButton)

        // set a click listener on the button
        this.floatingActionButton.setOnClickListener { view ->
            this.addNewItemDialog()
        }

        this.database = FirebaseDatabase.getInstance().reference
    }

    /*
     * This method shows a dialog box where the user can enter a new item to add to their To Do list.
     */
    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)

        alert.setMessage("Add New Item")
        alert.setTitle("Enter To Do Item")

        alert.setView(itemEditText)

        alert.setPositiveButton("Submit") {dialog, positiveButton ->
            // Create new todoItem instance, initialised with default values
            val todoItem = ToDoItem.create()
            todoItem.itemText = itemEditText.text.toString()
            todoItem.done = false

            // Make a push to the database so that a new item is made with a unique id
            // Using push() method, get a new id from Firebase which is set on the todoItem
            val newItem = this.database.child(Constants.FIREBASE_ITEM).push()
            todoItem.objectId = newItem.key

            // todoItem is saved in Firebase database
            newItem.setValue(todoItem)

            dialog.dismiss()
            Toast.makeText(this, "Item saved with id " + todoItem.objectId,
                    Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }
}