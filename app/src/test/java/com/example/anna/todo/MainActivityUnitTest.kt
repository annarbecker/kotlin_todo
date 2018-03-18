package com.example.anna.todo

import android.content.Context
import com.google.firebase.FirebaseApp
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class MainActivityUnitTest {


    @Test
    fun test_mainActivityTitle() {
        val context: Context = RuntimeEnvironment.application.baseContext
        FirebaseApp.initializeApp(context)

        val mainActivity = Robolectric.setupActivity(MainActivity::class.java)
        val title = mainActivity.title.toString()
        assertThat(title, `is`("ToDo"))
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
