package com.example.anna.todo

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by anna on 3/11/18.
 */
class ToDoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // enable Firebase persistence for ofline access
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}