package com.example.anna.todo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by arbecker on 3/17/18.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentationTest {

    @Rule @JvmField
    val testRule: ActivityTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java)

    @Test
    fun opensItemEntryDialog() {
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withText(Constants.ADD_NEW_ITEM)).check(matches(isDisplayed()))
    }



}