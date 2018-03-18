package com.example.anna.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), ItemRowListener {
    // get access to Firebase database, no need of any URL, Firebase identifies the connection via
    // the package name of the app
    private lateinit var databaseReference: DatabaseReference
    private var toDoItemList: MutableList<ToDoItem>? = null
    private lateinit var toDoAdapter: ToDoItemAdapter
    private var listViewItems: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // define floating action button
        val floatingActionButton = this.findViewById<View>(R.id.addNewItemButton)
        this.listViewItems = findViewById<View>(R.id.items_list) as ListView

        // set a click listener on the button
        floatingActionButton.setOnClickListener { _ ->
            this.addNewItemDialog()
        }

        this.databaseReference = FirebaseDatabase.getInstance().reference
        this.toDoItemList = mutableListOf()
        this.toDoAdapter = ToDoItemAdapter(this, this.toDoItemList!!)
        this.listViewItems!!.adapter = this.toDoAdapter

        // fetch items from the database
        this.databaseReference.orderByKey().addListenerForSingleValueEvent(this.itemListener)
    }

    // show a dialog box where the user can enter a new item to add to their To Do list
    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)

        alert.setMessage(Constants.ADD_NEW_ITEM)
        alert.setTitle(Constants.ENTER_ITEM)

        alert.setView(itemEditText)

        alert.setPositiveButton(Constants.SUBMIT) {dialog, _ ->
            val toDoItem = ToDoItem.create()
            toDoItem.itemText = itemEditText.text.toString()

            // make a push to the database so that a new item is made with a unique id
            // using push() method, get a new id from Firebase which is set on the todoItem
            val newItem = this.databaseReference.child(Constants.FIREBASE_ITEM).push()
            toDoItem.objectId = newItem.key
            
            // toDoItem is saved in Firebase database
            newItem.setValue(toDoItem)

            dialog.dismiss()

            // add listener for items added after to do list is created
            this.databaseReference.orderByKey().addListenerForSingleValueEvent(this.itemListener)

        }
        alert.show()
    }

    private var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // update UI with database changes (item added/removed/done state changed)
            updateToDoListData(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError?) {
            // getting item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError!!.toException())
        }
    }

    private fun updateToDoListData(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()

        // check if current database contains any collection
        if(items.hasNext()) {
            // clear the list
            this.toDoItemList!!.clear()

            val toDoListIndex = items.next()
            val itemsIterator = toDoListIndex.children.iterator()

            // check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                // get current item
                val currentItem = itemsIterator.next()
                val toDoItem = ToDoItem.create()

                // get current data in a map
                val map = currentItem.value as HashMap<String, Any>

                // key will return Firebase id
                toDoItem.objectId = currentItem.key
                toDoItem.done = map["done"] as Boolean?
                toDoItem.itemText = map["itemText"] as String?

                // add item to list
                this.toDoItemList!!.add(toDoItem)
            }
        }
        else {
            // if the database doesn't contain any items clear the list
            this.toDoItemList!!.clear()
        }

        // alert adapter that data has changed
        this.toDoAdapter.notifyDataSetChanged()
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
        // add listener for items changed or removed after to do list is created
        this.databaseReference.orderByKey().addListenerForSingleValueEvent(this.itemListener)
        return this.databaseReference.child(Constants.FIREBASE_ITEM).child(itemObjectId)
    }

    override fun onDestroy() {
        this.databaseReference.removeEventListener(this.itemListener)
        super.onDestroy()
    }
}