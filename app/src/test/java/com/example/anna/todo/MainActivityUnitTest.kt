package com.example.anna.todo

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*

class MainActivityUnitTest {
    @Test
    fun createNewToDoItem() {
        val mainActivity = MainActivity()
        val itemText = "Walk the dog"

        val toDoItem = mainActivity.createNewToDoItem(itemText)
        toDoItem.objectId = "firebaseAssignedObjectId"

        assertThat(toDoItem.itemText, `is`(itemText))
        assertThat(toDoItem.objectId, `is`("firebaseAssignedObjectId"))
    }
}
