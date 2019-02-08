package com.example.agu.ma_assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class CompListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mRecyclerLayoutMan;
    private ArrayList<String> CompNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_list);

        CompNames.add("Hello0");
        CompNames.add("Hello1");
        CompNames.add("Hello2");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerLayoutMan = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mRecyclerLayoutMan);

        // specify an adapter (see also next example)
        mRecyclerAdapter= new RecyclerViewAdapter(CompNames);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
