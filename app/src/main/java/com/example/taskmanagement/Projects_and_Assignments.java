package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Projects_and_Assignments extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_and_assignments);

        ListView proj_and_assign_list = findViewById(R.id.proj_and_assign_list);

        //Add Array items to ListView using an ArrayAdapter
        ArrayAdapter<Proj_and_assign_POJO> projAndAssignAdapter = new ArrayAdapter<Proj_and_assign_POJO>(this,
                android.R.layout.simple_list_item_1, Proj_and_assign_POJO.proj_assign);
        proj_and_assign_list.setAdapter(projAndAssignAdapter);

        AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
            Intent intent = null;

            if(position == 0)
                intent = new Intent(Projects_and_Assignments.this, Projects.class);
            else if (position == 1)
                intent = new Intent(Projects_and_Assignments.this, Assignments.class);
            startActivity(intent);
        };

        proj_and_assign_list.setOnItemClickListener(itemClickListener);
    }
}