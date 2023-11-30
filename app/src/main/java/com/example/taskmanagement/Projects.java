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

public class Projects extends AppCompatActivity {
    private ListView projects_list_view;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Gson projectsGson;
    private EditText txtProjects;
    public static ArrayList<Project> projectsArrayList = new ArrayList<Project>();
    public static ArrayList<Project>  doneProjectsArrayList = new ArrayList<Project>();
    //_____________________________________________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        setupSharedPreferences();   //Setup SharedPreferences, Editor and Gson
        setupViews();   //Setup the needed elements that we will need in this class

        fillLists();    //Fill the Arraylists
        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
        savedProjects();    //Set saved projects to be true(done) in the ListView
    }
    //_____________________________________________________________________________________________________________________________________

    private void putGsonString(){   //This method is to put Gson String for ArrayList in the file
        String projectsString = projectsGson.toJson(projectsArrayList);    //Convert from ArrayList to Json String
        editor.putString("projects", projectsString);   //Put Json String in the editor file
        editor.commit();    //Save changes
    }
    //_____________________________________________________________________________________________________________________________________

    private void fillLists() {  //Get values for the ArrayLists from the file
        String strProjects = preference.getString("projects", "");  //Get the string value from file
        String strDoneProjects = preference.getString("doneProjects", "");  //Get the string value from file

        if(strProjects.equalsIgnoreCase(""))
            initializeArrayList();
        else {
            projectsArrayList = projectsGson.fromJson(strProjects,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Project>>() {}.getType());

            if(!strDoneProjects.equalsIgnoreCase("")) {
                doneProjectsArrayList = projectsGson.fromJson(strDoneProjects,   //Convert from Json String to ArrayList
                        new TypeToken<ArrayList<Project>>() {}.getType());
            }
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private void initializeArrayList() {    //Add initial values to the ArrayList
        projectsArrayList.clear();
        projectsArrayList.add(new Project("Android"));
        projectsArrayList.add(new Project("AI"));
        projectsArrayList.add(new Project("Web"));
        projectsArrayList.add(new Project("ML"));
        projectsArrayList.add(new Project("OS"));
        projectsArrayList.add(new Project("QA"));
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupSharedPreferences() { //Setup SharedPreferences, Editor and Gson
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        projectsGson = new Gson();
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupViews() { //Setup the needed elements that we will need in this class
        ImageView projects_image = (ImageView) findViewById(R.id.projects_image);//This image will be added at run time(dynamically)
        projects_image.setImageResource(R.drawable.projects);
        projects_list_view = (ListView) findViewById(R.id.projects_list_view);
        txtProjects = (EditText) findViewById(R.id.txtProjects);
    }
    //_____________________________________________________________________________________________________________________________________

    private void fromArrayListToListView() {    //Add ArrayList items to ListView using an ArrayAdapter
            ArrayAdapter<Project> projectsAdapter = new ArrayAdapter<Project>(this,
                    android.R.layout.simple_list_item_multiple_choice, projectsArrayList);
            projects_list_view.setAdapter(projectsAdapter);
    }
    //_____________________________________________________________________________________________________________________________________

    private boolean itemIsExisted(String data) {  // This method is to check if a project is existed in the ArrayList or not
        Project p = new Project(data);
        for(Project proj : projectsArrayList){
            if(proj.getName().equalsIgnoreCase(p.getName()))
                return true;
        }
        return false;   //Not existed
    }
    //_____________________________________________________________________________________________________________________________________

    private void savedProjects(){   //Set saved projects to be true(done) in the ListView
        for (Project p : doneProjectsArrayList) {
            int position = getPositionOfItem(p.getName().toString());
            projects_list_view.setItemChecked(position, true);
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private int getPositionOfItem(String data) {    //This method is to get the position of a project in the ListView
        for(int i = 0 ; i < projects_list_view.getCount() ; i++){
            if(projects_list_view.getItemAtPosition(i).toString().trim().equalsIgnoreCase(data))
                return i;
        }
        return -1;  //If the project is not existed
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnSavedOnClickProjects(View view) {    //To save the done projects in the editor file
        String doneProjectsString = "";
        doneProjectsArrayList.clear();

        for(int i = 0 ; i < projects_list_view.getCount(); i++){    //Add all checked projects to doneProjectsArrayList
            if(projects_list_view.isItemChecked(i))
                doneProjectsArrayList.add(new Project(projects_list_view.getItemAtPosition(i).toString()));
        }

        doneProjectsString = projectsGson.toJson(doneProjectsArrayList);    //Convert from ArrayList to Json String
        editor.putString("doneProjects", doneProjectsString);   //Put Json String in the editor file
        editor.commit();    //Save changes

        if(!doneProjectsArrayList.isEmpty())  //if there are done projects
            Toast.makeText(this, "Done Projects:\n" + doneProjectsString, Toast.LENGTH_SHORT).show();
        else    //if there are no done projects
            Toast.makeText(this, "No projects have been done.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnLoadOnClickProjects(View view) {     //To load the number of done projects using preference and gson
        String str = preference.getString("doneProjects", "");  //Get the string value from file

        if(!doneProjectsArrayList.isEmpty()) {  //if there are done projects
            ArrayList<Project> doneProjectsArrayList = projectsGson.fromJson(str,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Project>>() {}.getType());

            Toast.makeText(this, "The number of done projects is: " + doneProjectsArrayList.size(),
                    Toast.LENGTH_SHORT).show();
        }
        else    //if there are no done projects
            Toast.makeText(this, "No projects have been done.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnInsertOnClickProjects(View view) {
        String item = txtProjects.getText().toString().trim();

        if(!item.equals("")){   //if there is an item in the EditText
            if(!itemIsExisted(item)){ //if this item is not existed in the ListView
                projectsArrayList.add(new Project(item));
                putGsonString();    //Put Gson String for ArrayList in the file
                fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter

                //if it was in doneProjectsArrayList remove it
                for(Project proj : doneProjectsArrayList){
                    if(proj.getName().toString().trim().equalsIgnoreCase(item)) {
                        doneProjectsArrayList.remove(proj);
                        break;
                    }
                }
                savedProjects();    //Set saved projects to be true(done) in the ListView

                Toast.makeText(this, "The project " + item + " has been added successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this item is existed in the ListView
                Toast.makeText(this, "The project " + item + " is existed before.", Toast.LENGTH_SHORT).show();
        }
        else    //if there is no item in the EditText
            Toast.makeText(this, "Sorry, you have to enter a project name.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnDeleteOnClickProjects(View view) {
        String item = txtProjects.getText().toString().trim();

        if(!item.equals("")){   //if there is an item in the EditText
            if(itemIsExisted(item)){    //if this item is existed in the ListView
                for(Project proj : projectsArrayList){
                    if(proj.getName().equalsIgnoreCase(item)) {
                        //if it was true(done) make it false(not) and remove it from doneProjectsArrayList
                        int position = getPositionOfItem(item);
                        if(doneProjectsArrayList.contains(proj)) {
                            doneProjectsArrayList.remove(proj);
                            projects_list_view.setItemChecked(position, false);
                        }
                        projectsArrayList.remove(proj);
                        putGsonString();    //Put Gson String for ArrayList in the file
                        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
                        savedProjects();    //Set saved projects to be true(done) in the ListView
                        break;
                    }
                }

                Toast.makeText(this, "The project " + item + " has been deleted successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this item is not existed in the ListView
                Toast.makeText(this, "This project is not existed.", Toast.LENGTH_SHORT).show();
        }
        else    //if there is no item in the EditText
            Toast.makeText(this, "Sorry, you have to enter a project name.", Toast.LENGTH_SHORT).show();
    }
}