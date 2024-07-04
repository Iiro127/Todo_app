/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.todo_app.presentation

import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.todo_app.R
import com.example.todo_app.presentation.adapter.ToDoAdapter
import com.example.todo_app.presentation.data.ToDoItem
import com.example.todo_app.presentation.db.TodoHandler
import com.example.todo_app.presentation.theme.Todo_appTheme
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: ToDoAdapter
    private lateinit var TodoList: ArrayList<ToDoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)

        adapter = ToDoAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            showAddToDoDialog()
        }

        loadTodos()
    }

    fun readAll(): ArrayList<ToDoItem> {
        return TodoHandler(this@MainActivity).readAll()
    }

    fun loadTodos(): Boolean {
        try {
            TodoList = readAll()
            adapter = ToDoAdapter(TodoList)
            recyclerView.adapter = adapter
            return true
        } catch (e: Exception){
            Toast.makeText(this@MainActivity, "Unable to load todos", Toast.LENGTH_SHORT)
            return false
        }
    }

    private fun showAddToDoDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add, null)
        builder.setCustomTitle(dialogView)

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val todoText = input.text.toString()
            if (todoText.isNotEmpty()) {
                val newTodo = ToDoItem(null, todoText, false)
                TodoHandler(this@MainActivity).create(newTodo)
                Toast.makeText(this@MainActivity, "ToDo item added", Toast.LENGTH_SHORT).show()

                loadTodos()
            } else {
                Toast.makeText(this@MainActivity, "Empty fields", Toast.LENGTH_SHORT).show()
            }
            /*if (todoText.isNotEmpty()) {
                val todoItem = ToDoItem(todoText, false)
                adapter.addItem(todoItem)
            }*/
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
