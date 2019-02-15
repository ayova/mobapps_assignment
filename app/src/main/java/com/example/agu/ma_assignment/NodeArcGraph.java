package com.example.agu.ma_assignment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NodeArcGraph extends AppCompatActivity {

    NodeArcGenerator v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_node_arc_graph);

        v = new NodeArcGenerator(this);
        setContentView(v);
        getIntentData();
    }

    private void getIntentData(){
        if(getIntent().hasExtra("compName") && getIntent().hasExtra("compNumber")){
            Log.d("intentExtra", "getIntentData: "+getIntent().getStringExtra("compName"));
            Log.d("intentExtra", "getIntentData: "+getIntent().getStringExtra("compNumber"));
        }
    }

}
