package com.example.anna.todo

import java_cup.Main
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class)
class MainActivityUnitTest {


    @Test
    fun test_MainActivity() {
        val mainActivity = Robolectric.setupActivity(MainActivity::class.java)
        val title = mainActivity.actionBar.title.toString()

        assertThat(title, `is`("ToDo2"))
    }


    @Test
    fun test_createNewToDoItem() {
        val mainActivity = MainActivity()

        val itemText = "Walk the dog"

        val toDoItem = mainActivity.createNewToDoItem(itemText)
        toDoItem.objectId = "firebaseAssignedObjectId"

        assertThat(toDoItem.itemText, `is`(itemText))
        assertThat(toDoItem.objectId, `is`("firebaseAssignedObjectId"))
    }
}
