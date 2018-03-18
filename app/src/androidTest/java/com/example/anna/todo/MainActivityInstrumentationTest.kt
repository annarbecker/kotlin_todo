package com.example.anna.todo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
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
    val testRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun opensItemEntryDialog() {
        Intents.init()

        onView(withId(R.id.addNewItemButton)).perform(click())
        onView(withText(Constants.ADD_NEW_ITEM)).check(matches(isDisplayed()))
        onView(withText(Constants.ENTER_ITEM)).check(matches(isDisplayed()))
        onView(withText(Constants.SUBMIT)).check(matches(isDisplayed()))

        Intents.release()
    }
}