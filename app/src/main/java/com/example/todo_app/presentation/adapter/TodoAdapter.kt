package com.example.todo_app.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.presentation.data.ToDoItem

class ToDoAdapter(private val todoList: MutableList<ToDoItem>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodo: TextView = itemView.findViewById(R.id.tv_todo)
        val cbDone: CheckBox = itemView.findViewById(R.id.cb_done)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todoItem = todoList[position]
        holder.tvTodo.text = todoItem.text
        holder.cbDone.isChecked = todoItem.isDone

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            todoItem.isDone = isChecked
        }
    }

    override fun getItemCount(): Int = todoList.size

    fun addItem(todoItem: ToDoItem) {
        todoList.add(todoItem)
        notifyItemInserted(todoList.size - 1)
    }
}
