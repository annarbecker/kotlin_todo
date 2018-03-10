package com.example.anna.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define floating action button
        val floatingActionButton = this.findViewById<View>(R.id.floatingActionButton)

        // set a click listener on the button
        this.floatingActionButton.setOnClickListener { view ->
            this.addNewItemDialog()
        }
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

        alert.setPositiveButton("Submit") {dialog, positiveButton -> }
        alert.show()
    }
}