package com.example.anna.todo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //define floating action button
        val floatingButton = this.findViewById<View>(R.id.floatingButton)

        // set a click listener on the button
        this.floatingButton.setOnClickListener { view ->

        }
    }
}
