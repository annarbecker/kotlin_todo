# kotlin_todo
To do list written in Kotlin.


* Have a button to show a dialog where we can enter simple text and submit, so that a new item is 
added
* The newly added item should be shown in a listview with a checkbox on each row
* When this checkbox is clicked, it should mark this item as done
* When we load data from firebase, it will also tell us if the item is done, in which case, we will 
just show the checkbox as checked
* When we long press the list item, we will see a context menu with ‘Update’ and ‘Delete’ options
* On Update option selected, dialog will again appear with text already filled in and we can update 
the text and submit it
* On Delete option selected, the item will be deleted from the app and the Firebase database

**Notes:**
* Use the `ToDoItemAdapter` to extract data from the `ToDoItem` object, then assign object fields' 
values to the views in each row of the `ListView` 