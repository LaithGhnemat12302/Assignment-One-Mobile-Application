package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Prayers extends AppCompatActivity {
    private ListView listView;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Gson prayersGson;
    private String strPrayers = "";
    public static ArrayList<Pray> prayersArrayList = new ArrayList<Pray>();
    public static ArrayList<Pray> donePrayersArrayList = new ArrayList<Pray>();
    //_____________________________________________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayers);

        setupSharedPreferences();   //Setup SharedPreferences, Editor and Gson
        setupViews();   //Setup the needed elements that we will need in this class

        fillLists();    //Fill the Arraylists
        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
        donePrayers();
    }
    //_____________________________________________________________________________________________________________________________________

    private void fillLists() {  //Get values for the ArrayLists from the file
        String strDonePrayers = preference.getString("donePrayers", "");  //Get the string value from file

        if(strPrayers.equalsIgnoreCase("")){    //Will happen only the first time
            initializeArrayList();
            strPrayers = "prayers";
        }

        if(!strDonePrayers.equalsIgnoreCase("")) {
            donePrayersArrayList = prayersGson.fromJson(strDonePrayers,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Pray>>() {}.getType());
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private void initializeArrayList() {    //Add initial values to the ArrayList
        prayersArrayList.clear();
        prayersArrayList.add(new Pray("Al-Fajr prayer"));
        prayersArrayList.add(new Pray("Aduhor prayer"));
        prayersArrayList.add(new Pray("Al-Asr prayer"));
        prayersArrayList.add(new Pray("Al-Maghreb prayer"));
        prayersArrayList.add(new Pray("Al-Eshaa prayer"));
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupSharedPreferences() { //Setup SharedPreferences, Editor and Gson
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        prayersGson = new Gson();
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupViews() { //Setup the needed elements that we will need in this class
        ImageView pray_image = (ImageView) findViewById(R.id.pray_image);//This image will be added at run time(dynamically)
        pray_image.setImageResource(R.drawable.pray_image);
        listView = (ListView) findViewById(R.id.listView);
    }
    //_____________________________________________________________________________________________________________________________________

    private void fromArrayListToListView() {    //Add ArrayList items to ListView using an ArrayAdapter
        ArrayAdapter<Pray> prayersAdapter = new ArrayAdapter<Pray>(this,
                android.R.layout.simple_list_item_multiple_choice, prayersArrayList);
        listView.setAdapter(prayersAdapter);
    }
    //_____________________________________________________________________________________________________________________________________

    private void donePrayers(){   //Set done prayers to be true in the ListView
        for(Pray pray : donePrayersArrayList){
                int position = getPositionOfItem(pray.getName().toString().trim());
                listView.setItemChecked(position, true);
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private int getPositionOfItem(String data) {    //This method is to get the position of a pray in the ArrayList
        for(int i = 0 ; i < prayersArrayList.size() ; i++){
            if(prayersArrayList.get(i).getName().toString().trim().equalsIgnoreCase(data))
                return i;
        }
        return -1;  //If the pray is not existed
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickDone(View view) {    //To save the done prayers in the editor file
        String donePrayersString = "";
        donePrayersArrayList.clear();

        for(Pray pray : prayersArrayList){
            int position = getPositionOfItem(pray.getName());

            if(listView.isItemChecked(position))
                donePrayersArrayList.add(pray);
        }

        donePrayersString = prayersGson.toJson(donePrayersArrayList);    //Convert from ArrayList to Json String
        editor.putString("donePrayers", donePrayersString);   //Put Json String in the editor file
        editor.commit();    //Save changes

        if(!donePrayersArrayList.isEmpty())  //if there are done prayers
            Toast.makeText(this, "Done Prayers:\n" + donePrayersString, Toast.LENGTH_SHORT).show();
        else    //if there are no done prayers
            Toast.makeText(this, "No prayers have been done.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickLoad(View view) {     //To load the number of done prayers using preference and gson
        String str = preference.getString("donePrayers", "");  //Get the string value from file

        if(!donePrayersArrayList.isEmpty()) {  //if there are done prayers
            ArrayList<Pray> donePrayersArrayList = prayersGson.fromJson(str,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Pray>>() {}.getType());

            Toast.makeText(this, "The number of done prayers is: " + donePrayersArrayList.size(),
                    Toast.LENGTH_SHORT).show();
        }
        else    //if there are no done prayers
            Toast.makeText(this, "No prayers have been done.", Toast.LENGTH_SHORT).show();
    }
}