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

public class Football extends AppCompatActivity {
    private ListView listView;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Gson footballGson;
    private EditText txtFirstTeam;
    private EditText txtSecondTeam;
    private EditText txtMatchTime;
    public static ArrayList<Match> matchesArrayList = new ArrayList<Match>();
    public static ArrayList<Match> watchedMatchesArrayList = new ArrayList<Match>();
    //_____________________________________________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football);

        setupSharedPreferences();   //Setup SharedPreferences, Editor and Gson
        setupViews();   //Setup the needed elements that we will need in this class

        fillLists();    //Fill the Arraylists
        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
        watchedMatches();
    }
    //_____________________________________________________________________________________________________________________________________

    private void putGsonString(){   //This method is to put Gson String for ArrayList in the file
        String footballString = footballGson.toJson(matchesArrayList);    //Convert from ArrayList to Json String
        editor.putString("matches", footballString);   //Put Json String in the editor file
        editor.commit();    //Save changes
    }
    //_____________________________________________________________________________________________________________________________________

    private void fillLists() {  //Get values for the ArrayLists from the file
        String strMatches = preference.getString("matches", "");  //Get the string value from file
        String strWatchedMatches = preference.getString("watchedMatches", "");  //Get the string value from file

        if(strMatches.equalsIgnoreCase(""))
            initializeArrayList();
        else{
            matchesArrayList = footballGson.fromJson(strMatches,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Match>>() {}.getType());

            if(!strWatchedMatches.equalsIgnoreCase("")) {
                watchedMatchesArrayList = footballGson.fromJson(strWatchedMatches,   //Convert from Json String to ArrayList
                        new TypeToken<ArrayList<Match>>() {}.getType());
            }
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private void initializeArrayList() {    //Add initial values to the ArrayList
        matchesArrayList.clear();
        matchesArrayList.add(new Match("Barcelona","Real Madrid", "5:15"));
        matchesArrayList.add(new Match("Man City","Liverpool", "2:30"));
        matchesArrayList.add(new Match("PSG","Bayern Munich", "9:00"));
        matchesArrayList.add(new Match("Chelsea","Arsenal", "6:30"));
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupSharedPreferences() { //Setup SharedPreferences, Editor and Gson
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        footballGson = new Gson();
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupViews() { //Setup the needed elements that we will need in this class
        ImageView footballImage = (ImageView) findViewById(R.id.footballImage);//This image will be added at run time(dynamically)
        footballImage.setImageResource(R.drawable.football);
        listView = (ListView) findViewById(R.id.listView);
        txtFirstTeam = (EditText) findViewById(R.id.txtFirstTeam);
        txtSecondTeam = (EditText) findViewById(R.id.txtSecondTeam);
        txtMatchTime = (EditText) findViewById(R.id.txtMatchTime);
    }
    //_____________________________________________________________________________________________________________________________________

    private void fromArrayListToListView() {    //Add ArrayList items to ListView using an ArrayAdapter
        ArrayAdapter<Match> matchesAdapter = new ArrayAdapter<Match>(this,
                android.R.layout.simple_list_item_multiple_choice, matchesArrayList);
        listView.setAdapter(matchesAdapter);
    }
    //_____________________________________________________________________________________________________________________________________

    private boolean itemIsExisted(Match match) {  // This method is to check if a match is existed in the ArrayList or not
        for(Match m : matchesArrayList){
            if(m.getFirstTeam().equalsIgnoreCase(match.getFirstTeam()) &&
                    m.getSecondTeam().equalsIgnoreCase(match.getSecondTeam()) &&
                    m.getTime().equalsIgnoreCase(match.getTime()))
                return true;
        }
        return false;   //Not existed
    }
    //_____________________________________________________________________________________________________________________________________

    private void watchedMatches(){   //Set watched matches to be true in the ListView
        for(Match match : watchedMatchesArrayList){
                int position = getPositionOfItem(match);
                listView.setItemChecked(position, true);
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private int getPositionOfItem(Match match) {    //This method is to get the position of a match in the ArrayList
        for(int i = 0 ; i < matchesArrayList.size() ; i++) {
            if (matchesArrayList.get(i).getFirstTeam().equalsIgnoreCase(match.getFirstTeam()) &&
                    matchesArrayList.get(i).getSecondTeam().equalsIgnoreCase(match.getSecondTeam()) &&
                    matchesArrayList.get(i).getTime().equalsIgnoreCase(match.getTime()))
                return i;
        }
        return -1;  //If the match is not existed
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnWatchedOnClick(View view) {    //To save the watched matches in the editor file
        String watchedMatchesString = "";
        watchedMatchesArrayList.clear();

        for(Match match : matchesArrayList){
            int position = getPositionOfItem(match);
            if(listView.isItemChecked(position))
                watchedMatchesArrayList.add(match);
        }

        watchedMatchesString = footballGson.toJson(watchedMatchesArrayList);    //Convert from ArrayList to Json String
        editor.putString("watchedMatches", watchedMatchesString);   //Put Json String in the editor file
        editor.commit();    //Save changes

        if(!watchedMatchesArrayList.isEmpty())  //if there are watched matches
            Toast.makeText(this, "Watched matches:\n" + watchedMatchesString, Toast.LENGTH_SHORT).show();
        else    //if there are no watched matches
            Toast.makeText(this, "No matches have been watched.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnLoadOnClick(View view) {     //To load the number of watched matches using preference and gson
        String str = preference.getString("watchedMatches", "");  //Get the string value from file

        if(!watchedMatchesArrayList.isEmpty()) {  //if there are watched matches
            ArrayList<Match> watchedMatchesArrayList = footballGson.fromJson(str,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Match>>() {}.getType());

            Toast.makeText(this, "The number of watched matches is: " + watchedMatchesArrayList.size(),
                    Toast.LENGTH_SHORT).show();
        }
        else    //if there are no watched matches
            Toast.makeText(this, "No matches have been watched.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnInsertOnClick(View view) {
        String firstTeam = txtFirstTeam.getText().toString().trim();
        String secondTeam = txtSecondTeam.getText().toString().trim();
        String time = txtMatchTime.getText().toString().trim();

        if(!firstTeam.equals("") && !secondTeam.equals("") && !time.equals("")){   //if there is a match
            Match match = new Match(firstTeam, secondTeam, time);

            if(!itemIsExisted(match)){ //if this match is not existed in the ListView
                matchesArrayList.add(match);
                putGsonString();    //Put Gson String for ArrayList in the file
                fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter

                //if it was in watchedMatchesArrayList remove it
                for(Match m : watchedMatchesArrayList){
                    if(m.getFirstTeam().equalsIgnoreCase(match.getFirstTeam()) &&
                       m.getSecondTeam().equalsIgnoreCase(match.getSecondTeam()) &&
                       m.getTime().equalsIgnoreCase(match.getTime())) {
                            watchedMatchesArrayList.remove(m);
                            break;
                    }
                }
                watchedMatches();

                Toast.makeText(this, "The match has been added successfully.", Toast.LENGTH_SHORT).show();
            }
            else    //if this match is existed in the ListView
                Toast.makeText(this, "This match is existed before.", Toast.LENGTH_SHORT).show();
        }
        else    //if there is no match
            Toast.makeText(this, "Sorry, you have to fill all the fields.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnDeleteOnClick(View view) {
        String firstTeam = txtFirstTeam.getText().toString().trim();
        String secondTeam = txtSecondTeam.getText().toString().trim();
        String time = txtMatchTime.getText().toString().trim();

        if(!firstTeam.equals("") && !secondTeam.equals("") && !time.equals("")){   //if there is a match
            Match match = new Match(firstTeam, secondTeam, time);

            if(itemIsExisted(match)){    //if this match is existed in the ListView
                for(Match m : matchesArrayList){
                    if(m.getFirstTeam().equalsIgnoreCase(match.getFirstTeam()) &&
                       m.getSecondTeam().equalsIgnoreCase(match.getSecondTeam()) &&
                       m.getTime().equalsIgnoreCase(match.getTime())) {
                        //if it was true(watched) make it false(not watched) and remove it from watchedMatchesArrayList
                            int position = getPositionOfItem(match);
                            if(watchedMatchesArrayList.contains(m)) {
                                watchedMatchesArrayList.remove(m);
                                listView.setItemChecked(position, false);
                            }
                        matchesArrayList.remove(m);
                        putGsonString();    //Put Gson String for ArrayList in the file
                        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
                        watchedMatches();
                        break;
                    }
                }

                Toast.makeText(this, "The match has been deleted successfully.", Toast.LENGTH_SHORT).show();
            }
            else    //if this match is not existed in the ListView
                Toast.makeText(this, "This match is not existed.", Toast.LENGTH_SHORT).show();
        }
        else     //if there is no match
            Toast.makeText(this, "Sorry, you have to fill all fields.", Toast.LENGTH_SHORT).show();
    }
}