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

public class Midterms extends AppCompatActivity {
    private ListView listView;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Gson testsGson;
    private EditText txtTest;
    private EditText txtExamDay;
    public static ArrayList<Test> testsArrayList = new ArrayList<Test>();
    public static ArrayList<Test> finishedTestsArrayList = new ArrayList<Test>();
    //_____________________________________________________________________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midterms);

        setupSharedPreferences();   //Setup SharedPreferences, Editor and Gson
        setupViews();   //Setup the needed elements that we will need in this class

        fillLists();    //Fill the Arraylists
        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
        finishedTests();
    }
    //_____________________________________________________________________________________________________________________________________

    private void putGsonString(){   //This method is to put Gson String for ArrayList in the file
        String testsString = testsGson.toJson(testsArrayList);    //Convert from ArrayList to Json String
        editor.putString("midterms", testsString);   //Put Json String in the editor file
        editor.commit();    //Save changes
    }
    //_____________________________________________________________________________________________________________________________________

    private void fillLists() {  //Get values for the ArrayLists from the file
        String strTests = preference.getString("midterms", "");  //Get the string value from file
        String strFinishedTests = preference.getString("finishedMidterms", "");  //Get the string value from file

        if(strTests.equalsIgnoreCase(""))
            initializeArrayList();
        else{
            testsArrayList = testsGson.fromJson(strTests,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Test>>() {}.getType());

            if(!strFinishedTests.equalsIgnoreCase("")) {
                finishedTestsArrayList = testsGson.fromJson(strFinishedTests,   //Convert from Json String to ArrayList
                        new TypeToken<ArrayList<Test>>() {}.getType());
            }
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private void initializeArrayList() {    //Add initial values to the ArrayList
        testsArrayList.clear();
        testsArrayList.add(new Test("Web","Saturday"));
        testsArrayList.add(new Test("AI", "Tuesday"));
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupSharedPreferences() { //Setup SharedPreferences, Editor and Gson
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        testsGson = new Gson();
    }
    //_____________________________________________________________________________________________________________________________________

    private void setupViews() { //Setup the needed elements that we will need in this class
        ImageView examImage = (ImageView) findViewById(R.id.examImage);//This image will be added at run time(dynamically)
        examImage.setImageResource(R.drawable.midterm);
        listView = (ListView) findViewById(R.id.listView);
        txtTest = (EditText) findViewById(R.id.txtTest);
        txtExamDay = (EditText) findViewById(R.id.txtExamDay);
    }
    //_____________________________________________________________________________________________________________________________________

    private void fromArrayListToListView() {    //Add ArrayList items to ListView using an ArrayAdapter
        ArrayAdapter<Test> testsAdapter = new ArrayAdapter<Test>(this,
                android.R.layout.simple_list_item_multiple_choice, testsArrayList);
        listView.setAdapter(testsAdapter);
    }
    //_____________________________________________________________________________________________________________________________________

    private boolean itemIsExisted(String data) {  // This method is to check if a test is existed in the ArrayList or not
        for(Test test : testsArrayList){
            if(test.getTest().equalsIgnoreCase(data))
                return true;
        }
        return false;   //Not existed
    }
    //_____________________________________________________________________________________________________________________________________

    private void finishedTests(){   //Set finished tests to be true(done) in the ListView
        for(Test test : finishedTestsArrayList){
            int position = getPositionOfItem(test.getTest().toString().trim());
            listView.setItemChecked(position, true);
        }
    }
    //_____________________________________________________________________________________________________________________________________

    private int getPositionOfItem(String data) {    //This method is to get the position of a test in the ArrayList
        for(int i = 0 ; i < testsArrayList.size() ; i++){
            if(testsArrayList.get(i).getTest().toString().trim().equalsIgnoreCase(data))
                return i;
        }
        return -1;  //If the test is not existed
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickFinish(View view) {    //To save the finished tests in the editor file
        String finishedTestsString = "";
        finishedTestsArrayList.clear();

        for(Test test : testsArrayList){
            int position = getPositionOfItem(test.getTest());
            if(listView.isItemChecked(position))
                finishedTestsArrayList.add(test);
        }

        finishedTestsString = testsGson.toJson(finishedTestsArrayList);    //Convert from ArrayList to Json String
        editor.putString("finishedMidterms", finishedTestsString);   //Put Json String in the editor file
        editor.commit();    //Save changes

        if(!finishedTestsArrayList.isEmpty())  //if there are finished tests
            Toast.makeText(this, "Finished Tests:\n" + finishedTestsString, Toast.LENGTH_SHORT).show();
        else    //if there are no finished tests
            Toast.makeText(this, "No tests have been finished.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickLoad(View view) {     //To load the number of finished tests using preference and gson
        String str = preference.getString("finishedMidterms", "");  //Get the string value from file

        if(!finishedTestsArrayList.isEmpty()) {  //if there are finished tests
            ArrayList<Test> finishedTestsArrayList = testsGson.fromJson(str,   //Convert from Json String to ArrayList
                    new TypeToken<ArrayList<Test>>() {}.getType());

            Toast.makeText(this, "The number of finished tests is: " + finishedTestsArrayList.size(),
                    Toast.LENGTH_SHORT).show();
        }
        else    //if there are no finished tests
            Toast.makeText(this, "No tests have been finished.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickInsert(View view) {
        String testName = txtTest.getText().toString().trim();
        String testDay = txtExamDay.getText().toString().trim();

        if(!testName.equals("") && !testDay.equals("")){   //if there is a test
            if(!itemIsExisted(testName)){ //if this test is not existed in the ListView
                Test test = new Test(testName, testDay);
                testsArrayList.add(test);
                putGsonString();    //Put Gson String for ArrayList in the file
                fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter

                //if it was in finishedTestsArrayList remove it
                for(Test t : finishedTestsArrayList){
                    if(t.getTest().equalsIgnoreCase(test.getTest())) {
                        finishedTestsArrayList.remove(t);
                        break;
                    }
                }
                finishedTests();

                Toast.makeText(this, "The test " + testName + " has been added successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this test is existed in the ListView
                Toast.makeText(this, "The test " + testName + " is existed before.",
                        Toast.LENGTH_SHORT).show();
        }
        else    //if there is no test
            Toast.makeText(this, "Sorry, you have to fill all the fields.", Toast.LENGTH_SHORT).show();
    }
    //_____________________________________________________________________________________________________________________________________

    public void btnOnClickDelete(View view) {
        String testName = txtTest.getText().toString().trim();

        if(!testName.equals("")){   //if there is a test in the EditText
            if(itemIsExisted(testName)){    //if this test is existed in the ListView
                for(Test test : testsArrayList){
                    if(test.getTest().equalsIgnoreCase(testName)) {
                        //if it was true(finished) make it false(not finished) and remove it from finishedTestsArrayList
                        int position = getPositionOfItem(testName);
                        if(finishedTestsArrayList.contains(test)) {
                            finishedTestsArrayList.remove(test);
                            listView.setItemChecked(position, false);
                        }
                        testsArrayList.remove(test);
                        putGsonString();    //Put Gson String for ArrayList in the file
                        fromArrayListToListView();  //Add ArrayList items to ListView using an ArrayAdapter
                        finishedTests();
                        break;
                    }
                }

                Toast.makeText(this, "The test " + testName + " has been deleted successfully.",
                        Toast.LENGTH_SHORT).show();
            }
            else    //if this test is not existed in the ListView
                Toast.makeText(this, "This test is not existed.", Toast.LENGTH_SHORT).show();
        }
        else {    //if there is no test in the EditText
            Toast.makeText(this, "Sorry, you have to enter a test name in the test field.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}