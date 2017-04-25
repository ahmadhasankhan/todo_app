package com.example.todos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.todos.data.DatabaseHelper;
import com.example.todos.data.TodosContract;
import com.example.todos.data.TodosContract.TodosEntry;

public class TodoListActivity extends AppCompatActivity {

    String[] itemname = {
            "Get theatre tickets",
            "Order pizza for tonight",
            "Buy groceries",
            "Running session at 19.30",
            "Call Uncle Sam"
    };

    private void createCategory(){
        ContentValues values = new ContentValues();
        values.put(TodosContract.CategoriesEntry.COLUMN_DESCRIPTION, "Works");
        Uri uri = getContentResolver().insert(TodosContract.CategoriesEntry.CONTENT_URI, values);
        Log.d("MainActivity", "Inserted category " + uri);
    }

    private void updateTodo(){

    }

    private void readData() {
        String[] projection = {
                TodosEntry.COLUMN_TEXT,
                TodosEntry.COLUMN_CREATED,
                TodosEntry.COLUMN_EXPIRED,
                TodosEntry.COLUMN_DONE,
                TodosContract.CategoriesEntry.COLUMN_DESCRIPTION
        };

        String selection = TodosEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {"1"};
        Cursor c = getContentResolver().query(TodosEntry.CONTENT_URI, projection, null, null, null);
        int i = c.getCount();

        Log.d("Record Count", String.valueOf(i));
        String rowContent = "";
        while (c.moveToNext()) {
            for (i = 0; i <= 4; i++) {
                rowContent += c.getString(i) + "-";
            }
            Log.i("Row" + String.valueOf(c.getPosition()), rowContent);
            rowContent = "";
        }
        c.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DatabaseHelper helper = new DatabaseHelper(this);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        CreateTodo();
        //readData();
        //createCategory();
        readData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ListView lv = (ListView) findViewById(R.id.lvTodos);
        //adds the custom layout
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.todo_list_item,
                R.id.tvNote, itemname));
        //adds the click event to the listView, reading the content
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(TodoListActivity.this, TodoActivity.class);
                String content = (String) lv.getItemAtPosition(pos);
                intent.putExtra("Content", content);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void CreateTodo() {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String query = "INSERT INTO todos ("
                + TodosEntry.COLUMN_TEXT + ","
                + TodosEntry.COLUMN_CATEGORY + ","
                + TodosEntry.COLUMN_CREATED + ","
                + TodosEntry.COLUMN_EXPIRED + ","
                + TodosEntry.COLUMN_DONE + ")"
                + "VALUES(\"Go to office\", 1, \"2017-02-02\", \"\", 0)";
        db.execSQL(query);

        ContentValues values = new ContentValues();
        values.put(TodosEntry.COLUMN_TEXT, "Call Wife");
        values.put(TodosEntry.COLUMN_CATEGORY, 1);
        values.put(TodosEntry.COLUMN_CREATED, "2018-09-09");
        values.put(TodosEntry.COLUMN_DONE, 0);
        long todo_id = db.insert(TodosEntry.TABLE_NAME, null, values);
    }

}