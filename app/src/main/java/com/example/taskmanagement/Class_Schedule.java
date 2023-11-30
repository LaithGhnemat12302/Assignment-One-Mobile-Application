package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Class_Schedule extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        ListView daysListView = findViewById(R.id.daysListView);

        //Add Array items to ListView using an ArrayAdapter
        ArrayAdapter<Day> daysAdapter = new ArrayAdapter<Day>(this,
                android.R.layout.simple_list_item_1, Day.days);
        daysListView.setAdapter(daysAdapter);

        AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
            Intent intent = null;

            if(position == 0)
                intent = new Intent(Class_Schedule.this, saturday_Schedule.class);
            else if (position == 1)
                intent = new Intent(Class_Schedule.this, monday_Schedule.class);
            else if (position == 2)
                intent = new Intent(Class_Schedule.this, tuesday_Schedule.class);
            else if (position == 3)
                intent = new Intent(Class_Schedule.this, wednesday_Schedule.class);
            else if (position == 4)
                intent = new Intent(Class_Schedule.this, thursday_Schedule.class);
            startActivity(intent);
        };

        daysListView.setOnItemClickListener(itemClickListener);
    }
}