package com.example.agu.ma_assignment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

// API KEY: sic7yu4YhmpLToiLXPT7bGS_QvXW8SjIPLuhIfoF
// Request method is always GET for the Companies' House API
// GET https://api.companieshouse.gov.uk/search/companies
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void GoToSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    //temporary button
    public void NodeOpen(View view) {
        Intent intent = new Intent(this, NodeArcGraph.class);
        startActivity(intent);
    }
}
