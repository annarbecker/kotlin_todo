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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // get Access to Firebase database, no need of any URL, Firebase identifies the connection via
    // the package name of the app
    private lateinit var database: DatabaseReference
    private var toDoItemList: MutableList<ToDoItem>? = null
    private lateinit var adapter: ToDoItemAdapter
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
        this.toDoItemList = mutableListOf<ToDoItem>()
        this.adapter = ToDoItemAdapter(this, this.toDoItemList!!)
        this.listViewItems!!.adapter = this.adapter

        // fetch items from the database
        // add a listener for current database which will fetch and alert the function when the data
        // has been fetched
        this.database.orderByKey().addListenerForSingleValueEvent(this.itemListener)
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

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // get Post object and use the values to update the UI
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()

        // check if current database contains any collection
        if(items.hasNext()) {
            val toDoListIndex = items.next()
            val itemsIterator = toDoListIndex.children.iterator()

            // check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                // get current item
                val currentItem = itemsIterator.next()
                val toDoItem = ToDoItem.create()

                // get current data in a map
                val map = currentItem.getValue() as HashMap<String, Any>

                // key will return Firebase id
                toDoItem.objectId = currentItem.key
                toDoItem.done = map["done"] as Boolean?
                toDoItem.itemText = map["itemText"] as String?

                this.toDoItemList!!.add(toDoItem)
            }
        }

        // alert adapter that data has changed
        this.adapter.notifyDataSetChanged()
    }
}