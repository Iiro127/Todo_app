package com.example.todo_app.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.presentation.MainActivity
import com.example.todo_app.presentation.data.ToDoItem
import com.example.todo_app.presentation.db.TodoHandler

private lateinit var todoHandler: TodoHandler
private lateinit var context: Context

class ToDoAdapter(private val todoList: MutableList<ToDoItem>, private val mainActivity: MainActivity) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodo: TextView = itemView.findViewById(R.id.tv_todo)
        val cbDone: CheckBox = itemView.findViewById(R.id.cb_done)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        context = parent.context
        todoHandler = TodoHandler(context)
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todoItem = todoList[position]
        holder.tvTodo.text = todoItem.text
        holder.cbDone.isChecked = intToBoolean(todoItem.isDone)

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            todoItem.isDone = booleanToInt(isChecked)
            todoHandler.changeFavourite(todoItem)
        }

        holder.tvTodo.setOnClickListener {
            Log.d("TodoLog", "TextView clicked: ${todoItem.text}")
            // Handle the click event here
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            todoItem.id?.let { it1 -> todoHandler.delete(it1) }

            mainActivity.loadTodos()
        }
    }

    private fun intToBoolean(input: Int): Boolean {
        if (input == 1) {
            return true
        }

        return false
    }

    private fun booleanToInt(input: Boolean): Int{
        if (input) {
            return 1
        }

        return 0
    }

    override fun getItemCount(): Int = todoList.size
}
