package com.example.anna.todo

/**
 * Created by anna on 3/10/18.
 */
class ToDoItem {
    companion object Factory {
        fun create(): ToDoItem = ToDoItem()
    }

    // objectId holds the Firebase generated id. This is used to uniquely identify the item when
    // shown in the list
    var objectId: String? = null
    var itemText: String? = null
    var done: Boolean? = false
}