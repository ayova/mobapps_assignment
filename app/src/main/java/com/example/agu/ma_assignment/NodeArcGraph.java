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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

import javax.security.auth.login.LoginException;

public class NodeArcGraph extends AppCompatActivity {

    NodeArcGenerator v;
    private String CName;
    private String Cnumber;

    private ArrayList<String> officerName = new ArrayList<>();
    private ArrayList<String> officerDoB = new ArrayList<>();
    private ArrayList<String> officerNat = new ArrayList<>();
    private ArrayList<String> officerAddress = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = new NodeArcGenerator(this);
        setContentView(v);
        //getIntentData();

        // Set back button in title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    // function to go back to previous activity using top btn
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    // TODO: 01/03/2019: DELETE (AFTER ROOMS DB IMPLEMENTED!!!)
//    private void getIntentData(){
//        if(getIntent().hasExtra("compName") && getIntent().hasExtra("compNumber")){
//            CName = getIntent().getStringExtra("compName");
//            Cnumber = getIntent().getStringExtra("compNumber");
//            //Bundle gBundle = getIntent().getBundleExtra("myBundle");
//            //officerName = gBundle.getStringArrayList("offName");
//            officerName = getIntent().getStringArrayListExtra("offName");
//            for(int i=0; i < officerName.size() ; i++)//for testing purposes
//            {
//                Log.d("MSG_PASSED=>", "getOfficers: "+officerName.get(i));
//            }
//            Log.i("CompData", "Name; "+ CName + " Number; " + Cnumber);
//        }
//        else{
//            Log.d("IntentExtras", "getIntentData: EMPTY");
//        }
//    }
}
