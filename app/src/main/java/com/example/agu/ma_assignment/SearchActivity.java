package com.example.agu.ma_assignment;

import android.app.ActionBar;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    public ArrayList<String> CompanyNames = new ArrayList<String>();
    public ArrayList<String> CompanyNumber = new ArrayList<String>();
    public EditText userSearch;
    public String searchURL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        userSearch = findViewById(R.id.et_user_search);
        initRecycler();


        //in case null pointer produced:
        try{
            //display back button in title bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e){
            Log.e("BackButtonError", "Title bar back button: ",e );
        }
    }

    // function to go back to previous activity using intent
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    // Function used to retrieve all company names that suit the search
    public void api_search_all(View view) {
        //Create request queue
        RequestQueue rQueue = Volley.newRequestQueue(this);
        //JSONRequest
        //search url for company names
        searchURL = "https://api.companieshouse.gov.uk/search/companies?q=" + userSearch.getText().toString();

        JsonObjectRequest JSONretriever = new JsonObjectRequest(Request.Method.GET, searchURL, null,
                new Response.Listener < JSONObject > () {
                    @Override
                    public void onResponse(JSONObject response) { //what to do with the response from the API
                        try {
                            CompanyNames.clear();
                            CompanyNumber.clear();
                            JSONArray arr = response.getJSONArray("items");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject o = arr.getJSONObject(i);
                                String name = (o.getString("title")).toString(); //get company name
                                String number = (o.getString("company_number")).toString(); // get company number
                                //String address = (o.getString("address_snippet")).toString(); // get company address in one line
                                CompanyNames.add(i, name); //add name of the company to the array
                                CompanyNumber.add(i, number); //add number of the company to the array
                                initRecycler();
                            }
                        } catch (Exception e) { //if the response generates an exception, print it with printStackTrace()
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // if anything goes wrong... Print error to log
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSON", "JSON error: " + error.toString());
                    }
                }) {
            @Override
            public Map < String, String > getHeaders() throws AuthFailureError { // set the header for authentication (aka send API key)
                HashMap headers = new HashMap();
                headers.put("Authorization", "sic7yu4YhmpLToiLXPT7bGS_QvXW8SjIPLuhIfoF");
                return headers;
            }
        };
        //Finally, add the request to the request queue
        rQueue.add(JSONretriever);
    }

    private void initRecycler(){
        //create recycler to execute and set adapter + layoutmanager for it
        RecyclerView recyclerView = findViewById(R.id.recycler_display);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(CompanyNames,CompanyNumber,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void searchComp(View view) {
        try{
            AppDatabase db = Room.databaseBuilder(this,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
            db.officerDao().deleteAllOfficers();
            api_search_all(view);
        }
        catch (Exception e){
            Log.e("Error", "searchComp: " + e.getMessage());
        }
    }

    // TODO: 01/03/2019 IMPLEMENT ROOMS DATABASE
    // TODO: 01/03/2019 reminder (adapt companynames and no for DAO)
    // TODO: 01/03/2019 ADD SEARCH HISTORY - less important

}
