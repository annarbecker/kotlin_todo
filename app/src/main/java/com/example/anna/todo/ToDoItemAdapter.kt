package com.example.anna.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView

/**
 * Created by anna on 3/10/18.
 */
class ToDoItemAdapter(context: Context, toDoItemList: MutableList<ToDoItem>): BaseAdapter() {
    // val is like constant variable and its known as immutable in kotlin and can be initialized
    // only single time
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    // var is like general variable and its known as a mutable variable in kotlin and can be
    // assigned multiple times
    private var itemList = toDoItemList
    private var rowListener: ItemRowListener = context as ItemRowListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val objectId: String = this.itemList[position].objectId as String
        val itemText: String = this.itemList[position].itemText as String
        val done: Boolean = this.itemList[position].done as Boolean
        val view: View
        val listRowHolder: ListRowHolder

        if(convertView == null) {
            // inflate data model layout to View object
            view = this.inflater.inflate(R.layout.row_items, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
        }
        else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        // populate items with data
        listRowHolder.label.text = itemText
        listRowHolder.isDone.isChecked = done

        listRowHolder.isDone.setOnClickListener {
            this.rowListener.modifyItemState(objectId, !done)
        }

        listRowHolder.deleteButton.setOnClickListener{
            this.rowListener.onItemDelete(objectId)
        }

        return view
    }

    override fun getItem(index: Int): Any {
        return this.itemList[index]
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return this.itemList.size
    }

    // define ListRowHolder to contain items for the UI
    private class ListRowHolder(row: View?) {
        val label: TextView = row!!.findViewById(R.id.tv_item_text) as TextView
        val isDone: CheckBox = row!!.findViewById(R.id.cb_item_is_done) as CheckBox
        val deleteButton: ImageButton = row!!.findViewById(R.id.iv_cross) as ImageButton
    }
}