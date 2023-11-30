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

public class Languages extends AppCompatActivity {
    private ListView listView;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Gson languagesGson;
    private EditText txtLanguage;
    public static ArrayList<Language> languagesArrayList = new ArrayList<Language>();
    public static ArrayList<Language> savedLanguagesArrayList = new ArrayList<Language>();
    //_____________________________________________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        setupSharedPreferences();   //Setup SharedPreferences, Editor and Gson
        setupViews();   //Setup the needed elements that we will need in this class

        fillLists();    //Fill the Arraylists
        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
        savedLanguages();
    }
    //_____________________________________________________________________________________________________________________________________

    private void putGsonString(){   //This method is to put Gson String for ArrayList in the file
        String languagesString = languagesGson.toJson(languagesArrayList);    //Convert from ArrayList to Json String
        editor.putString("languages", languagesString);   //Put Json String in the editor file
        editor.commit();    //Save changes
    }
    //_____________________________________________________________________________________________________________________________________

    private void fillLists() {  //Get values for the ArrayLists from the file
        String strLanguages = preference.getString("languages", "");  //Get the string value from file
        String strSavedLanguages = preference.getString("savedLanguages", "");  //Get the string value from file

        if(strLanguages.equalsIgnoreCase(""))
            initializeArrayList();
        else{
            languagesArrayList = languagesGson.fromJson(strLanguages,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Language>>() {}.getType());

            if(!strSavedLanguages.equalsIgnoreCase("")) {
                savedLanguagesArrayList = languagesGson.fromJson(strSavedLanguages,   //Convert from Json String to ArrayList
                        new TypeToken<ArrayList<Language>>() {}.getType());
            }
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private void initializeArrayList() {    //Add initial values to the ArrayList
        languagesArrayList.clear();
        languagesArrayList.add(new Language("Java"));
        languagesArrayList.add(new Language("C"));
        languagesArrayList.add(new Language("Python"));
        languagesArrayList.add(new Language("JS"));
        languagesArrayList.add(new Language("C#"));
        languagesArrayList.add(new Language("php"));
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupSharedPreferences() { //Setup SharedPreferences, Editor and Gson
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        languagesGson = new Gson();
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupViews() { //Setup the needed elements that we will need in this class
        ImageView languageImage = (ImageView) findViewById(R.id.languageImage);//This image will be added at run time(dynamically)
        languageImage.setImageResource(R.drawable.languages_learning);
        listView = (ListView) findViewById(R.id.listView);
        txtLanguage = (EditText) findViewById(R.id.txtLanguage);
    }
    //_____________________________________________________________________________________________________________________________________

    private void fromArrayListToListView() {    //Add ArrayList items to ListView using an ArrayAdapter
        ArrayAdapter<Language> languagesAdapter = new ArrayAdapter<Language>(this,
                android.R.layout.simple_list_item_multiple_choice, languagesArrayList);
        listView.setAdapter(languagesAdapter);
    }
    //_____________________________________________________________________________________________________________________________________

    private boolean itemIsExisted(String data) {  // This method is to check if a language is existed in the ArrayList or not
        for(Language language : languagesArrayList){
            if(language.getLanguage().equalsIgnoreCase(data))
                return true;
        }
        return false;   //Not existed
    }
    //_____________________________________________________________________________________________________________________________________

    private void savedLanguages(){   //Set saved languages to be true in the ListView
        for(Language language : savedLanguagesArrayList){
                int position = getPositionOfItem(language.getLanguage().toString().trim());
                listView.setItemChecked(position, true);
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private int getPositionOfItem(String data) {    //This method is to get the position of a language in the ArrayList
        for(int i = 0 ; i < listView.getCount() ; i++){
            if(listView.getItemAtPosition(i).toString().trim().equalsIgnoreCase(data))
                return i;
        }
        return -1;  //If the language is not existed
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickSave(View view) {    //To save the languages in the editor file
        String savedLanguagesString = "";
        savedLanguagesArrayList.clear();

        for(Language language : languagesArrayList){
            int position = getPositionOfItem(language.getLanguage());
            if(listView.isItemChecked(position))
                savedLanguagesArrayList.add(language);
        }

        savedLanguagesString = languagesGson.toJson(savedLanguagesArrayList);    //Convert from ArrayList to Json String
        editor.putString("savedLanguages", savedLanguagesString);   //Put Json String in the editor file
        editor.commit();    //Save changes

        if(!savedLanguagesArrayList.isEmpty())  //if there are saved languages
            Toast.makeText(this, "saved Languages:\n" + savedLanguagesString, Toast.LENGTH_SHORT).show();
        else    //if there are no saved languages
            Toast.makeText(this, "No languages have been saved.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickLoad(View view) {     //To load the number of saved languages using preference and gson
        String str = preference.getString("savedLanguages", "");  //Get the string value from file

        if(!savedLanguagesArrayList.isEmpty()) {  //if there are saved languages
            ArrayList<Language> savedLanguagesArrayList = languagesGson.fromJson(str,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Language>>() {}.getType());

            Toast.makeText(this, "The number of saved languages is: " + savedLanguagesArrayList.size(),
                    Toast.LENGTH_SHORT).show();
        }
        else    //if there are no saved languages
            Toast.makeText(this, "No languages have been saved.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickInsert(View view) {
        String languageName = txtLanguage.getText().toString().trim();

        if(!languageName.equals("")){   //if there is a language
            if(!itemIsExisted(languageName)){ //if this language is not existed in the ListView
                languagesArrayList.add(new Language(languageName));
                putGsonString();    //Put Gson String for ArrayList in the file
                fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter

                //if it was in savedLanguagesArrayList remove it
                for(Language language : savedLanguagesArrayList){
                    if(language.getLanguage().toString().trim().equalsIgnoreCase(languageName)) {
                        savedLanguagesArrayList.remove(language);
                        break;
                    }
                }
                savedLanguages();

                Toast.makeText(this, "The language " + languageName + " has been added successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this language is existed in the ListView
                Toast.makeText(this, "The language " + languageName + " is existed before.",
                        Toast.LENGTH_SHORT).show();
        }
        else    //if there is no language
            Toast.makeText(this, "Sorry, you have to enter a language in the field.",
                    Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickDelete(View view) {
        String languageName = txtLanguage.getText().toString().trim();

        if(!languageName.equals("")){   //if there is a language in the EditText
            if(itemIsExisted(languageName)){    //if this language is existed in the ListView
                for(Language language : languagesArrayList){
                    if(language.getLanguage().equalsIgnoreCase(languageName)) {
                        //if it was true(saved) make it false and remove it from savedLanguagesArrayList
                        int position = getPositionOfItem(languageName);
                        if(savedLanguagesArrayList.contains(language)) {
                            savedLanguagesArrayList.remove(language);
                            listView.setItemChecked(position, false);
                        }
                        languagesArrayList.remove(language);
                        putGsonString();    //Put Gson String for ArrayList in the file
                        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
                        savedLanguages();
                        break;
                    }
                }

                Toast.makeText(this, "The language " + languageName + " has been deleted successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this language is not existed in the ListView
                Toast.makeText(this, "This language is not existed.", Toast.LENGTH_SHORT).show();
        }
        else {    //if there is no language in the EditText
            Toast.makeText(this, "Sorry, you have to enter a language name in the language field.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}