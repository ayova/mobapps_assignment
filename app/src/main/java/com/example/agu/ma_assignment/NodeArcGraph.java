package com.example.agu.ma_assignment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

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

public class NodeArcGraph extends AppCompatActivity {

    NodeArcGenerator v;
    private String CName;
    private int Cnumber;

    private ArrayList<String> officerName = new ArrayList<>();
    private ArrayList<String> officerDoB = new ArrayList<>();
    private ArrayList<String> officerNat = new ArrayList<>();
    private ArrayList<String> officerAddress = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_node_arc_graph);

        v = new NodeArcGenerator(this);
        setContentView(v);
        getIntentData();

        // Set back button in title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get officers
        api_search_officers(v);

    }


    // function to go back to previous activity using intent
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    //get comp name and number the user clicked on
    private void getIntentData(){
        if(getIntent().hasExtra("compName") && getIntent().hasExtra("compNumber")){
            CName = getIntent().getStringExtra("compName");
            Cnumber = Integer.parseInt(getIntent().getStringExtra("compNumber"));
            Log.i("CompData", "Name; "+ CName + " Number; " + Cnumber);

        }
        else{
            Log.d("IntentExtras", "getIntentData: EMPTY");
        }
    }

    private void api_search_officers(View view){
        //Create request queue
        RequestQueue rQueue = Volley.newRequestQueue(this);
        //JSONRequest
        //search url for company names
        String searchURL = "https://api.companieshouse.gov.uk/company/" + Cnumber + "/officers?register_type=directors";
        JsonObjectRequest JSONretriever = new JsonObjectRequest(Request.Method.GET, searchURL, null,
                new Response.Listener < JSONObject > () {
                    @Override
                    public void onResponse(JSONObject response) { //what to do with the response from the API

                        try {
                            JSONArray arr = response.getJSONArray("items");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject o = arr.getJSONObject(i);
                                //add officer data to relevant array
                                String name = o.getString("name");
                                officerName.add(name);
                                Log.i("logging data", "onResponse: " + name);

                                //officer's nationality
                                String nationality;
                                if(!o.has("nationality")){
                                    nationality = "Unavailable";
                                    officerNat.add(nationality);
                                }
                                else{
                                    nationality = o.getString("nationality");
                                    officerNat.add(nationality);
                                }
                                Log.i("logging data", "onResponse: " + nationality);

                                //officer's year of birth
                                JSONObject dob = o.getJSONObject("date_of_birth");
                                String yr;
                                if(!dob.has("year")){
                                    yr = "Unavailable";
                                    officerDoB.add(yr);
                                }
                                else {
                                    yr = dob.getString("year");
                                    officerDoB.add(yr);
                                }
                                Log.i("logging data", "onResponse: " + yr);

                                //officer's address
                                JSONObject addr = o.getJSONObject("address");
                                String postCode, cty, stNo, street, ctry, ofAddress;
                                if(!addr.has("postal_code") || !addr.has("locality") || !addr.has("premises") || !addr.has("address_line_1") || !addr.has("country")){
                                    ofAddress = "Unavailable";
                                    officerAddress.add(ofAddress);

                                }
                                else {
                                    postCode = addr.getString("postal_code");
                                    cty = addr.getString("locality");
                                    stNo = addr.getString("premises");
                                    street = addr.getString("address_line_1");
                                    ctry = addr.getString("country");
                                    ofAddress = stNo + " " + street + ", " + cty + ", " + postCode + ". " + ctry;
                                    officerAddress.add(ofAddress);
                                }
                                Log.i("logging data", "onResponse: " + ofAddress);
                            }
                        } catch (Exception e) { //if the response generates an exception, print it with printStackTrace()
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // if anything goes wrong... Print error to log
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e("JSON", "JSON error: " + networkResponse.statusCode);
                        //                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
//                            // HTTP Status Code: 401 Unauthorized
//                        }
                    }
                }) {
            @Override
            public Map< String, String > getHeaders() throws AuthFailureError { // set the header for authentication (aka send API key)
                HashMap headers = new HashMap();
                headers.put("Authorization", "sic7yu4YhmpLToiLXPT7bGS_QvXW8SjIPLuhIfoF");
                return headers;
            }
        };
        //Finally, add the request to the request queue
        rQueue.add(JSONretriever);
    }

}
