package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Exams extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        ListView examsListView = findViewById(R.id.examsListView);

        //Add Array items to ListView using an ArrayAdapter
        ArrayAdapter<Exam> examsAdapter = new ArrayAdapter<Exam>(this,
                android.R.layout.simple_list_item_1, Exam.exams);
        examsListView.setAdapter(examsAdapter);

        AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
            Intent intent = null;

            if(position == 0)
                intent = new Intent(Exams.this, Quizes.class);
            else if (position == 1)
                intent = new Intent(Exams.this, Midterms.class);
            else if (position == 2)
                intent = new Intent(Exams.this, Finals.class);

            startActivity(intent);
        };

        examsListView.setOnItemClickListener(itemClickListener);
    }
}