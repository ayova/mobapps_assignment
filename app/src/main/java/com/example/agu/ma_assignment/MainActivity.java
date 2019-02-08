package com.example.agu.ma_assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    public ArrayList<String> CompNames;
    public ListView listCompanies;

    public RelativeLayout relRec;

    public ArrayList<String> listItems=new ArrayList<String>();  //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    public ArrayAdapter<String> adapter; //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInput = (EditText) findViewById(R.id.et_companyName);
        //searchResult = (TextView) findViewById(R.id.search_result);
        btnSearch = (Button) findViewById(R.id.btn_search);
        relRec = (RelativeLayout) findViewById(R.id.relativeLayoutRecycler);
    }


    public void btnSearchClick (View view){
        try {
//            Intent launchCompList = new Intent(this, CompListActivity.class);
//            Log.d("btnclicked", "btnSearchClick: clicked");
//            startActivity(launchCompList);
            searchCompanies(view);
            initRecycler();

        }
        catch (Exception e){
            Log.e("btnclicked", "btnSearchClick: " +e );
        }

    }


    // All companies matching search:
    public void searchCompanies( View view){
        //Create request queue
        RequestQueue rQueue = Volley.newRequestQueue(this);
        //Create the actual request (JsonObjecRequest)
        Log.i("comp", API_URL+"search/companies?q="+userInput.getText().toString());
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
                                String name = (o.getString("title")).toString(); //get company name
                                String number = (o.getString("company_number")).toString(); // get company number
                                String address = (o.getString("address_snippet")).toString(); // get company address in one line
                                CompNames.add(name);
                                for(int j=0; j<CompNames.size(); j++){
                                    Log.i("compnames", "onResponse: "+CompNames.get(j));
                                }

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

    public void initRecycler(){
        Log.d("recycler", "initRecycler: preparing");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(CompNames,relRec, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
