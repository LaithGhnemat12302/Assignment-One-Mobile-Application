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

public class monday_Schedule extends AppCompatActivity {
    private ListView listView;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Gson daysGson;
    private EditText txtLecture;
    private EditText txtStartTime;
    private EditText txtFinishTime;
    public static ArrayList<Lecture> lecturesArrayList = new ArrayList<Lecture>();
    public static ArrayList<Lecture> finishedLecturesArrayList = new ArrayList<Lecture>();
    //_____________________________________________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monday_schedule);

        setupSharedPreferences();   //Setup SharedPreferences, Editor and Gson
        setupViews();   //Setup the needed elements that we will need in this class

        fillLists();    //Fill the Arraylists
        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
        finishedLectures();
    }
    //_____________________________________________________________________________________________________________________________________

    private void putGsonString(){   //This method is to put Gson String for ArrayList in the file
        String daysString = daysGson.toJson(lecturesArrayList);    //Convert from ArrayList to Json String
        editor.putString("mondayLectures", daysString);   //Put Json String in the editor file
        editor.commit();    //Save changes
    }
    //_____________________________________________________________________________________________________________________________________

    private void fillLists() {  //Get values for the ArrayLists from the file
        String strLectures = preference.getString("mondayLectures", "");  //Get the string value from file
        String strFinishedLectures = preference.getString("finishedLecturesMonday", "");  //Get the string value from file

        if(strLectures.equalsIgnoreCase(""))
            initializeArrayList();
        else {
            lecturesArrayList = daysGson.fromJson(strLectures,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Lecture>>() {}.getType());

            if(!strFinishedLectures.equalsIgnoreCase("")) {
                finishedLecturesArrayList = daysGson.fromJson(strFinishedLectures,   //Convert from Json String to ArrayList
                        new TypeToken<ArrayList<Lecture>>() {}.getType());
            }
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private void initializeArrayList() {    //Add initial values to the ArrayList
        lecturesArrayList.clear();
        lecturesArrayList.add(new Lecture("Web","8:00 - 8:50"));
        lecturesArrayList.add(new Lecture("AI", "10:50 - 11:35"));
        lecturesArrayList.add(new Lecture("OS", "11:45 - 12:30"));
        lecturesArrayList.add(new Lecture("Network Lab", "1:35 - 4:10"));
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupSharedPreferences() { //Setup SharedPreferences, Editor and Gson
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        daysGson = new Gson();
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupViews() { //Setup the needed elements that we will need in this class
        ImageView dayImage = (ImageView) findViewById(R.id.dayImage);//This image will be added at run time(dynamically)
        dayImage.setImageResource(R.drawable.monday);
        listView = (ListView) findViewById(R.id.listView);
        txtLecture = (EditText) findViewById(R.id.txtLecture);
        txtStartTime = (EditText) findViewById(R.id.txtStartTime);
        txtFinishTime = (EditText) findViewById(R.id.txtFinishTime);
    }
    //_____________________________________________________________________________________________________________________________________

    private void fromArrayListToListView() {    //Add ArrayList items to ListView using an ArrayAdapter
        ArrayAdapter<Lecture> lecturesAdapter = new ArrayAdapter<Lecture>(this,
                android.R.layout.simple_list_item_multiple_choice, lecturesArrayList);
        listView.setAdapter(lecturesAdapter);
    }
    //_____________________________________________________________________________________________________________________________________

    private boolean itemIsExisted(String data) {  // This method is to check if a lecture is existed in the ArrayList or not
        for(Lecture lecture : lecturesArrayList){
            if(lecture.getLecture().equalsIgnoreCase(data))
                return true;
        }
        return false;   //Not existed
    }
    //_____________________________________________________________________________________________________________________________________

    private void finishedLectures(){   //Set finished lectures to be true(done) in the ListView
        for(Lecture lecture : finishedLecturesArrayList){
            int position = getPositionOfItem(lecture.getLecture());
            listView.setItemChecked(position, true);
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private int getPositionOfItem(String data) {    //This method is to get the position of a lecture in the ArrayList
        for(int i = 0 ; i < lecturesArrayList.size() ; i++){
            if(lecturesArrayList.get(i).getLecture().equalsIgnoreCase(data))
                return i;
        }
        return -1;  //If the lecture is not existed
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnFinishedOnClickDay(View view) {    //To save the finished lectures in the editor file
        String finishedLecturesString = "";
        finishedLecturesArrayList.clear();

        for(Lecture lecture : lecturesArrayList){
            int position = getPositionOfItem(lecture.getLecture());
            if(listView.isItemChecked(position))
                finishedLecturesArrayList.add(lecture);
        }

        finishedLecturesString = daysGson.toJson(finishedLecturesArrayList);    //Convert from ArrayList to Json String
        editor.putString("finishedLecturesMonday", finishedLecturesString);   //Put Json String in the editor file
        editor.commit();    //Save changes

        if(!finishedLecturesArrayList.isEmpty())  //if there are finished lectures
            Toast.makeText(this, "Finished Lectures:\n" + finishedLecturesString, Toast.LENGTH_SHORT).show();
        else    //if there are no finished lectures
            Toast.makeText(this, "No lectures have been finished.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnLoadOnClickDay(View view) {     //To load the number of finished lectures using preference and gson
        String str = preference.getString("finishedLecturesMonday", "");  //Get the string value from file

        if(!finishedLecturesArrayList.isEmpty()) {  //if there are finished lectures
            ArrayList<Lecture> finishedLecturesArrayList = daysGson.fromJson(str,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Lecture>>() {}.getType());

            Toast.makeText(this, "The number of finished lectures is: " + finishedLecturesArrayList.size(),
                    Toast.LENGTH_SHORT).show();
        }
        else    //if there are no finished lectures
            Toast.makeText(this, "No lectures have been finished.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnInsertOnClickDay(View view) {
        String name = txtLecture.getText().toString().trim();
        String startTime = txtStartTime.getText().toString().trim();
        String finishTime = txtFinishTime.getText().toString().trim();

        if(!name.equals("") && !startTime.equals("") && !finishTime.equals("")){   //if there is a lecture
            if(!itemIsExisted(name)){ //if this lecture is not existed in the ListView
                String time = startTime + " - " + finishTime;
                Lecture lecture = new Lecture(name, time);
                lecturesArrayList.add(lecture);
                putGsonString();    //Put Gson String for ArrayList in the file
                fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter

                //if it was in finishedLecturesArrayList remove it
                for(Lecture lec : finishedLecturesArrayList){
                    if(lec.getLecture().equalsIgnoreCase(lecture.getLecture())) {
                        finishedLecturesArrayList.remove(lec);
                        break;
                    }
                }
                finishedLectures();

                Toast.makeText(this, "The lecture " + name + " has been added successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this lecture is existed in the ListView
                Toast.makeText(this, "The lecture " + name + " is existed before.",
                        Toast.LENGTH_SHORT).show();
        }
        else    //if there is no lecture
            Toast.makeText(this, "Sorry, you have to fill all the fields.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnDeleteOnClickDay(View view) {
        String name = txtLecture.getText().toString().trim();

        if(!name.equals("")){   //if there is a lecture in the EditText
            if(itemIsExisted(name)){    //if this lecture is existed in the ListView
                for(Lecture lecture : lecturesArrayList){
                    if(lecture.getLecture().equalsIgnoreCase(name)) {
                        //if it was true(finished) make it false(not finished) and remove it from finishedLecturesArrayList
                        int position = getPositionOfItem(name);
                        if(finishedLecturesArrayList.contains(lecture)) {
                            finishedLecturesArrayList.remove(lecture);
                            listView.setItemChecked(position, false);
                        }
                        lecturesArrayList.remove(lecture);
                        putGsonString();    //Put Gson String for ArrayList in the file
                        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
                        finishedLectures();
                        break;
                    }
                }

                Toast.makeText(this, "The lecture " + name + " has been deleted successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this lecture is not existed in the ListView
                Toast.makeText(this, "This lecture is not existed.", Toast.LENGTH_SHORT).show();
        }
        else {    //if there is no lecture in the EditText
            Toast.makeText(this, "Sorry, you have to enter a lecture name in the lecture field.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}