package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mainListView = (ListView) findViewById(R.id.main_list);

        ArrayAdapter<Activity> mainListAdapter = new ArrayAdapter<Activity>(this, android.R.layout.simple_list_item_1, Activity.activities);
        mainListView.setAdapter(mainListAdapter);

        AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
            Intent intent = null;

            if(position == 0)
                intent = new Intent(MainActivity.this, Projects_and_Assignments.class);
            else if(position == 1)
                intent = new Intent(MainActivity.this, Football.class);
            else if(position == 2)
                intent = new Intent(MainActivity.this, Class_Schedule.class);
            else if(position == 3)
                intent = new Intent(MainActivity.this, Languages.class);
            else if(position == 4)
                intent = new Intent(MainActivity.this, Exams.class);
            else if(position == 5)
                intent = new Intent(MainActivity.this, Prayers.class);

            startActivity(intent);
        };

        mainListView.setOnItemClickListener(itemClickListener);
    }
}