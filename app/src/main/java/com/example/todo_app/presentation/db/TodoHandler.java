package com.example.todo_app.presentation.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.todo_app.presentation.data.ToDoItem;

import java.util.ArrayList;

public class TodoHandler extends DatabaseHelper {

    public TodoHandler(Context context) {
        super(context);
    }

    public boolean create(ToDoItem toDoItem){
        ContentValues values = new ContentValues();

        values.put("title", toDoItem.getText());
        values.put("isDone", toDoItem.isDone());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean success = db.insert("Todos", null, values) > 0;
        db.close();

        return success;
    }

    public ArrayList<ToDoItem> readAll(){
        ArrayList<ToDoItem> exercises = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Todos ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") Integer isDone = Integer.valueOf(cursor.getString(cursor.getColumnIndex("isDone")));

                ToDoItem todoItem = new ToDoItem(id, title, isDone);
                todoItem.setId(id);
                exercises.add(todoItem);
            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return exercises;
    }

    public boolean changeFavourite(ToDoItem toDoItem){
        ContentValues values = new ContentValues();

        values.put("title", toDoItem.getText());
        values.put("isDone", toDoItem.isDone());

        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.update("Todos", values, "id=?", new String[]{String.valueOf(toDoItem.getId())});
        Log.e("DB_UPDATE", "Updated rows: " + rowsAffected);
        db.close();

        return rowsAffected > 0;
    }

    public boolean delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete("Todos", "id='" + id + "'", null) > 0;
        db.close();

        return success;
    }
}
