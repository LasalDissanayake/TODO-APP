package com.example.to_do

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TasksApp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_PRIORITY = "priority"
        private const val COLUMN_DATETIME = "datetime" // Add this line for the datetime column
    }

    // Method called when the database is created
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_PRIORITY TEXT,$COLUMN_DATETIME TEXT)" // Add COLUMN_DATETIME to the query
        db?.execSQL(createTableQuery)
    }

    // Method called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }


    // Method to insert a task into the database
    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_CONTENT, task.content)
            put(COLUMN_PRIORITY, task.priority)
            put(COLUMN_DATETIME, task.dateTime)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Method to retrieve all tasks from the database
    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_DATETIME ASC"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))
            val dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATETIME)) // Add this line for datetime retrieval

            val task = Task(id, title, content,priority, dateTime) // Pass dateTime to Task constructor
            taskList.add(task)
        }
        cursor.close()
        db.close()
        return taskList
    }

    // Method to update a task in the database
    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_CONTENT, task.content)
            put(COLUMN_PRIORITY, task.priority)
            put(COLUMN_DATETIME, task.dateTime)

        }
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    // Method to retrieve a task from the database by its ID
    fun getTaskByID(taskId: Int): Task{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID= $taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()


        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))
        val datetime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATETIME))



        cursor.close()
        db.close()
        return Task(id, title ,content,priority,datetime)
    }

    // Method to delete a task from the database by its ID
    fun deleteTask(taskId:Int){
        val db=writableDatabase
        val whereClause="$COLUMN_ID=?"
        val whereArgs= arrayOf(taskId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }
}
