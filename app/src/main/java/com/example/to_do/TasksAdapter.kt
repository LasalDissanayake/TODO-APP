package com.example.to_do

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private var tasks: List<Task>, private val context: Context) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private val db:TaskDatabaseHelper= TaskDatabaseHelper(context)
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton:ImageView=itemView.findViewById(R.id.deleteButton)
    }

    // Method to create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    // Method to bind data to view holder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
        holder.priorityTextView.text = task.priority
        holder.dateTimeTextView.text = task.dateTime

        // Set up click listener for update button
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("task_id",task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        // Set up click listener for delete button
        holder.deleteButton.setOnClickListener {
            db.deleteTask(task.id)
            refreshData(db.getAllTasks())
            Toast.makeText(holder.itemView.context,"Task Deleted",Toast.LENGTH_SHORT).show()
        }
    }

    // Method to get item count
    override fun getItemCount(): Int = tasks.size

    // Method to refresh data
    fun refreshData(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
