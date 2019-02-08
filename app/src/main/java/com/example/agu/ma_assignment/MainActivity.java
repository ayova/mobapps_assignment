package com.example.agu.ma_assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


// API KEY: sic7yu4YhmpLToiLXPT7bGS_QvXW8SjIPLuhIfoF
// Request method is always GET for the Companies' House API
// GET https://api.companieshouse.gov.uk/search/companies
public class MainActivity extends AppCompatActivity {


    public EditText userInput;
    public ProgressBar searchProgress;
    public TextView searchResult;
    public String API_URL = "https://api.companieshouse.gov.uk/";
    public Button btnSearch;
    public String[][] compData = new String[20][3];
    public ListView listCompanies;

    ArrayList<String> listItems=new ArrayList<String>();  //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayAdapter<String> adapter; //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        userInput = (EditText) findViewById(R.id.et_companyName);
        btnSearch = (Button) findViewById(R.id.btn_search);
        //searchResult = (TextView) findViewById(R.id.search_result);

    }


    public void btnSearch (View view){
        Intent inent = new Intent(this, CompListActivity.class);

        // calling an activity using <intent-filter> action name
        //  Intent inent = new Intent("com.hmkcode.android.ANOTHER_ACTIVITY");

        startActivity(inent);
    }


    // All companies matching search:
    public void searchCompanies( View view){
        //Create request queue
        RequestQueue rQueue = Volley.newRequestQueue(this);
        //Create the actual request (JsonObjecRequest)
        Log.i("COMPINFO", API_URL+"search/companies?q="+userInput.getText().toString());
        String scUrl = API_URL + "search/companies?q=" + userInput.getText().toString();
        JsonObjectRequest JSONretriever = new JsonObjectRequest(
                Request.Method.GET, //method to send request
                scUrl,              //url - detailed above
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.i("responseJ", response.toString());
                            JSONArray arr = response.getJSONArray("items");
                            //Log.i("responseJ", arr.get(1).toString());
                            for(int i=0;i<arr.length();i++) {
                                JSONObject o = arr.getJSONObject(i);
                                String name = (o.getString("title")).toString();
                                String number = (o.getString("company_number")).toString();
                                String address = (o.getString("address_snippet")).toString();
                                Log.d("jsonresp", "Row: "+(i+1)+" "+name+" | "+number+" | "+address);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSON", "JSON error: " + error.toString());
                    }
                }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "sic7yu4YhmpLToiLXPT7bGS_QvXW8SjIPLuhIfoF");
                    return headers;
            }
        };
        //Add the request to the request queue
        rQueue.add(JSONretriever);
    }

}
