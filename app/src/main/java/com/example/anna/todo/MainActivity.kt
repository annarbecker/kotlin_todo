package com.example.anna.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), ItemRowListener {
    // get Access to Firebase database, no need of any URL, Firebase identifies the connection via
    // the package name of the app
    private lateinit var database: DatabaseReference
    private var toDoItemList: MutableList<ToDoItem>? = null
    private lateinit var toDoAdapter: ToDoItemAdapter
    private var listViewItems: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define floating action button
        val floatingActionButton = this.findViewById<View>(R.id.floatingActionButton)
        this.listViewItems = findViewById<View>(R.id.items_list) as ListView

        // set a click listener on the button
        floatingActionButton.setOnClickListener { view ->
            this.addNewItemDialog()
        }

        this.database = FirebaseDatabase.getInstance().reference
        this.toDoItemList = mutableListOf()
        this.toDoAdapter = ToDoItemAdapter(this, this.toDoItemList!!)
        this.listViewItems!!.adapter = this.toDoAdapter

        // fetch items from the database
        // add a listener for current database which will fetch and alert the function when the data
        // has been fetched
        this.database.orderByKey().addListenerForSingleValueEvent(this.itemListener)
    }


    // Shows a dialog box where the user can enter a new item to add to their To Do list.
    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)

        alert.setMessage(Constants.ADD_NEW_ITEM)
        alert.setTitle(Constants.ENTER_ITEM)

        alert.setView(itemEditText)

        alert.setPositiveButton("Submit") {dialog, positiveButton ->
            // Create new toDoItem instance, initialised with itemText and done
            val toDoItem = ToDoItem(itemEditText.text.toString(), false)

            // Make a push to the database so that a new item is made with a unique id
            // Using push() method, get a new id from Firebase which is set on the todoItem
            val newItem = this.database.child(Constants.FIREBASE_ITEM).push()
            toDoItem.objectId = newItem.key

            // toDoItem is saved in Firebase database
            newItem.setValue(toDoItem)

            dialog.dismiss()
            Toast.makeText(this, "Item saved with id " + toDoItem.objectId,
                    Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }

    private var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // get Post object and use the values to update the UI
            Log.d(localClassName, "New item added")
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    fun addDataToList(dataSnapshot: DataSnapshot) {
        if (dataSnapshot != null) {
            Log.d(this.localClassName, "Item added")
            val toDoList = arrayListOf<ToDoItem>()

            for (item in dataSnapshot.children) {
                val map = item.value as HashMap<String, Any>
                val itemText = map["itemText"] as String?
                val done = map["done"] as Boolean?

                if (itemText != null && done != null) {
                    val newItem = ToDoItem(itemText, done)
                    newItem.objectId = item.key

                    toDoList.add(newItem)
                }
            }

            // alert adapter that data has changed
            this.toDoAdapter.setList(toDoList)
        }
    }

    // implement methods from ItemRowListener interface
    override fun modifyItemState(itemObjectId: String, isDone: Boolean) {
        val itemReference = this.getDatabaseReference(itemObjectId)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemObjectId: String) {
        // get child reference in database using objectId
        val itemReference = this.getDatabaseReference(itemObjectId)

        // remove the value from the database
        itemReference.removeValue()
    }

    private fun getDatabaseReference(itemObjectId: String): DatabaseReference {
        return this.database.child(Constants.FIREBASE_ITEM).child(itemObjectId)
    }
}