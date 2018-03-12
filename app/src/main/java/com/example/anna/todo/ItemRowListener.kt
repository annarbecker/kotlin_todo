package com.example.anna.todo

/**
 * Created by anna on 3/11/18.
 */
interface ItemRowListener {

    fun modifyItemState(itemObjectId: String, isDone: Boolean)
    fun onItemDelete(itemObjectId: String)
}