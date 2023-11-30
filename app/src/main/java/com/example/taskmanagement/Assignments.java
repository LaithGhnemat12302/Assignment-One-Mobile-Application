package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Assignments extends AppCompatActivity {
    private ListView assignments_list_view;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Gson assignmentsGson;
    private EditText txtAssignments;
    public static ArrayList<Assignment> assignmentsArrayList = new ArrayList<Assignment>();
    public static ArrayList<Assignment>  doneAssignmentsArrayList = new ArrayList<Assignment>();
    //_____________________________________________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        setupSharedPreferences();   //Setup SharedPreferences, Editor and Gson
        setupViews();   //Setup the needed elements that we will need in this class

        fillLists();    //Fill the Arraylists
        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
        savedAssignments();
    }
    //_____________________________________________________________________________________________________________________________________

    private void putGsonString(){   //This method is to put Gson String for ArrayList in the file
        String assignmentsString = assignmentsGson.toJson(assignmentsArrayList);    //Convert from ArrayList to Json String
        editor.putString("assignments", assignmentsString);   //Put Json String in the editor file
        editor.commit();    //Save changes
    }
    //_____________________________________________________________________________________________________________________________________

    private void fillLists() {  //Get values for the ArrayLists from the file
        String strAssignments = preference.getString("assignments", "");  //Get the string value from file
        String strDoneAssignments = preference.getString("doneAssignments", "");  //Get the string value from file

        if(strAssignments.equalsIgnoreCase(""))
            initializeArrayList();
        else{
            assignmentsArrayList = assignmentsGson.fromJson(strAssignments,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Assignment>>() {}.getType());

            if(!strDoneAssignments.equalsIgnoreCase("")) {
                doneAssignmentsArrayList = assignmentsGson.fromJson(strDoneAssignments,   //Convert from Json String to ArrayList
                        new TypeToken<ArrayList<Assignment>>() {}.getType());
            }
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private void initializeArrayList() {    //Add initial values to the ArrayList
        assignmentsArrayList.clear();
        assignmentsArrayList.add(new Assignment("Network"));
        assignmentsArrayList.add(new Assignment("DB"));
        assignmentsArrayList.add(new Assignment("Java"));
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupSharedPreferences() { //Setup SharedPreferences, Editor and Gson
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        assignmentsGson = new Gson();
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupViews() { //Setup the needed elements that we will need in this class
        ImageView assignments_image = (ImageView) findViewById(R.id.assignments_image);//This image will be added at run time(dynamically)
        assignments_image.setImageResource(R.drawable.assignments);
        assignments_list_view = (ListView) findViewById(R.id.assignments_list_view);
        txtAssignments = (EditText) findViewById(R.id.txtAssignments);
    }
    //_____________________________________________________________________________________________________________________________________

    private void fromArrayListToListView() {    //Add ArrayList items to ListView using an ArrayAdapter
            ArrayAdapter<Assignment> assignmentsAdapter = new ArrayAdapter<Assignment>(this,
                    android.R.layout.simple_list_item_multiple_choice, assignmentsArrayList);
            assignments_list_view.setAdapter(assignmentsAdapter);
    }
    //_____________________________________________________________________________________________________________________________________

    private boolean itemIsExisted(String data) {  // This method is to check if an assignment is existed in the ArrayList or not
        Assignment assign = new Assignment(data);
        for(Assignment assignment : assignmentsArrayList){
            if(assignment.getName().equalsIgnoreCase(assign.getName()))
                return true;
        }
        return false;   //Not existed
    }
    //_____________________________________________________________________________________________________________________________________

    private void savedAssignments(){   //Set saved assignments to be true(done) in the ListView
        for (Assignment ass : doneAssignmentsArrayList) {
            int position = getPositionOfItem(ass.getName().toString());
            assignments_list_view.setItemChecked(position, true);
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private int getPositionOfItem(String data) {    //This method is to get the position of an assignment in the ListView
        for(int i = 0 ; i < assignments_list_view.getCount() ; i++){
            if(assignments_list_view.getItemAtPosition(i).toString().equalsIgnoreCase(data))
                return i;
        }
        return -1;  //If the assignment is not existed
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnSavedOnClickAssignments(View view) {    //To save the done assignments in the editor file
        String doneAssignmentsString = "";
        doneAssignmentsArrayList.clear();

        for(int i = 0 ; i < assignments_list_view.getCount(); i++){    //Add all checked assignments to doneAssignmentsArrayList
            if(assignments_list_view.isItemChecked(i))
                doneAssignmentsArrayList.add(new Assignment(assignments_list_view.getItemAtPosition(i).toString()));
        }

        doneAssignmentsString = assignmentsGson.toJson(doneAssignmentsArrayList);    //Convert from ArrayList to Json String
        editor.putString("doneAssignments", doneAssignmentsString);   //Put Json String in the editor file
        editor.commit();    //Save changes

        if(!doneAssignmentsArrayList.isEmpty())  //if there are done assignments
            Toast.makeText(this, "Done Assignments:\n" + doneAssignmentsString, Toast.LENGTH_SHORT).show();
        else    //if there are no done assignments
            Toast.makeText(this, "No assignments have been done.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnLoadOnClickAssignments(View view) {     //To load the number of done assignments using preference and gson
        String str = preference.getString("doneAssignments", "");  //Get the string value from file

        if(!doneAssignmentsArrayList.isEmpty()) {  //if there are done assignments
            ArrayList<Assignment> doneAssignmentsArrayList = assignmentsGson.fromJson(str,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Assignment>>() {}.getType());

            Toast.makeText(this, "The number of done assignments is: " + doneAssignmentsArrayList.size(),
                    Toast.LENGTH_SHORT).show();
        }
        else    //if there are no done assignments
            Toast.makeText(this, "No assignments have been done.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnInsertOnClickAssignments(View view) {
        String item = txtAssignments.getText().toString().trim();

        if(!item.equals("")){   //if there is an item in the EditText
            if(!itemIsExisted(item)){ //if this item is not existed in the ListView
                assignmentsArrayList.add(new Assignment(item));
                putGsonString();    //Put Gson String for ArrayList in the file
                fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter

                //if it was in doneAssignmentsArrayList remove it
                for(Assignment ass : doneAssignmentsArrayList){
                    if(ass.getName().toString().trim().equalsIgnoreCase(item)) {
                        doneAssignmentsArrayList.remove(ass);
                        break;
                    }
                }
                savedAssignments(); //Set saved assignments to be true(done) in the ListView

                Toast.makeText(this, "The assignment " + item + " has been added successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this item is existed in the ListView
                Toast.makeText(this, "The assignment " + item + " is existed before.", Toast.LENGTH_SHORT).show();
        }
        else    //if there is no item in the EditText
            Toast.makeText(this, "Sorry, you have to enter an assignment name.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnDeleteOnClickAssignments(View view) {
        String item = txtAssignments.getText().toString().trim();

        if(!item.equals("")){   //if there is an item in the EditText
            if(itemIsExisted(item)){    //if this item is existed in the ListView
                for(Assignment ass : assignmentsArrayList){
                    if(ass.getName().equalsIgnoreCase(item)) {
                        //if it was true(done) make it false(not) and remove it from doneAssignmentsArrayList
                        int position = getPositionOfItem(item);
                        if(doneAssignmentsArrayList.contains(ass)) {
                            doneAssignmentsArrayList.remove(ass);
                            assignments_list_view.setItemChecked(position, false);
                        }
                        assignmentsArrayList.remove(ass);
                        putGsonString();    //Put Gson String for ArrayList in the file
                        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
                        savedAssignments();
                        break;
                    }
                }

                Toast.makeText(this, "The assignment " + item + " has been deleted successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this item is not existed in the ListView
                Toast.makeText(this, "This assignment is not existed.", Toast.LENGTH_SHORT).show();
        }
        else    //if there is no item in the EditText
            Toast.makeText(this, "Sorry, you have to enter an assignment name.", Toast.LENGTH_SHORT).show();
    }
}