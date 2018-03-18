package com.example.anna.todo

import org.junit.Test

import org.junit.Assert.*

class MainActivityUnitTest {
    @Test
    fun createNewToDoItem() {
        val mainActivity = MainActivity()
        val itemText = "Walk the dog"

        val toDoItem = mainActivity.createNewToDoItem(itemText)

        assertEquals(itemText, toDoItem.itemText)
    }
}
