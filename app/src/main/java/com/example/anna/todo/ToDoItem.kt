package com.example.anna.todo

/**
 * Created by anna on 3/10/18.
 */
class ToDoItem {
    // this makes sure that the model can be instantiated
    companion object Factory {
        fun create(): ToDoItem = ToDoItem()
    }

    // objectId holds the Firebase generated id in the model. This is used to uniquely identify the
    // item when shown in a list
    var objectId: String? = null
    // contains to do item text
    var itemText: String? = null
    // represents completed state
    var done: Boolean? = false
}