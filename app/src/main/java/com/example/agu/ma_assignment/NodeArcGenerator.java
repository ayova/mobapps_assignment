package com.example.agu.ma_assignment;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class NodeArcGenerator extends View {

    private float x;
    private float y;
    private ArrayList<Node> Nodes = new ArrayList<>();
    private Node company = new Node(500,500, 50);
    private Canvas mCanvas;
    int officerCount;
    Context context;

    public NodeArcGenerator(Context context) {
        super(context);
        this.context = context;
        this.x = 0;
        this.y = 0;
    }

    private int getOfCount(){
        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
        officerCount = db.officerDao().getOfficerCount();
        return officerCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;



        Paint generic = new Paint();
        generic.setColor(Color.RED);
        //company node in the middle
        company.nodeGetX();
        company.nodeGetY();
        company.drawNode(canvas,company);
        //Log.i("node position", "onDraw: " + " x: "+company.nodeGetX() + " y: " + company.nodeGetY());
        company.getNodeArea(company);
    }

    public boolean nodeClicked(Node node){
        // TODO: 01/03/2019 when officer clicked, redirect to recycler view that shows the officer's data
        //get the area of the node
        float[] pos = node.getNodeArea(node);
        //check if click was inside boundaries of node
        if(this.x > pos[0] && this.x < pos[2] && this.y > pos[1] && this.y < pos[3]){
            return true;
        }
        else{ return false;}
    }

    public void drawAroundCompany(){
        float[] outter = company.getNodeArea(company);
        int left = (int) outter[0];
        int top = (int) outter[1];
        int right = (int) outter[2];
        int bottom = (int) outter[3];

        int x = left;
        int y = top+company.getNodeRadius();

        for (int i = 0; i < getOfCount(); i++) {
//            Node newOfficer = new Node();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // tap
                this.x = event.getX();
                this.y = event.getY();
                //Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);
                if(nodeClicked(company)){
                    Toast.makeText(this.getContext(), "Node clicked", Toast.LENGTH_SHORT).show();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE: // drag
                this.x = event.getX();
                this.y = event.getY();
                Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);
                if(nodeClicked(company)) {
                    company.nodeSetX(event.getX());
                    company.nodeSetY(event.getY());
                    company.drawNode(mCanvas, company);
                }
                invalidate();
                break;
        }


        this.postInvalidate();
        return true;
    }
}
