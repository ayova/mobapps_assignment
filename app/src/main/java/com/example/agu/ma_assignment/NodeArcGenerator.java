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
import java.util.List;

public class NodeArcGenerator extends View {

    final String TAG = "NodeArcGen";
    private float x;
    private float y;
    private ArrayList<Node> Nodes = new ArrayList<>();
    private Node company = new Node(0,0, 50);
    private Canvas mCanvas;
    Context context;
    List<Officer> ofNodeLs;

    public NodeArcGenerator(Context context) {
        super(context);
        this.context = context;
        this.x = 0;
        this.y = 0;
    }

    //draw the officer nodes around the company node that's in the very center
    private void drawOfficers(Canvas canvas){
        //connection with rooms database to retrieve officers data
        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
        ofNodeLs = db.officerDao().getAllOfficers();

        Paint generic = new Paint();
        generic.setColor(Color.RED);
        Log.d(TAG, "drawOfficers: count >> "+db.officerDao().getOfficerCount());
        if(ofNodeLs.isEmpty()){
            Toast.makeText(context, "No directors available", Toast.LENGTH_SHORT).show();
        }
        else {
            int angleIncrease = 360 / ofNodeLs.size(); //divide the circumference in as many angles as officers there are

            float cx = company.nodeGetX();
            float cy = company.nodeGetY();

    //        float scaleMarkSize = getResources().getDisplayMetrics().density * 16; // 16dp
            float radius = 500;

            for (int i = 0; i < 360; i += angleIncrease) {
                float angle = (float) Math.toRadians(i); // Need to convert to radians first

                float ofNodeX = (float) (cx + radius * Math.sin(angle));
                float ofNodeY = (float) (cy - radius * Math.cos(angle));

                for (int j = 0; j < ofNodeLs.size(); j++) {
                    Node ofNode = new Node(ofNodeX, ofNodeY, 25);
                    ofNode.drawNode(canvas, ofNode);
                    Nodes.add(j,ofNode);
                    canvas.drawLine(company.nodeGetX(), company.nodeGetY(), ofNodeX, ofNodeY, generic);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        //set canvas to the middle of the screen
        canvas.translate(getWidth()/2,getHeight()/2);
        //paint
        Paint generic = new Paint();
        generic.setColor(Color.RED);

        company.drawNode(canvas, company);
        drawOfficers(canvas);

    }

    public boolean nodeClicked(MotionEvent event){
        boolean ret = false;
        x = event.getX();
        y = event.getY();
        //get area for each node
        for (int i = 0; i < Nodes.size(); i++) {
            Node node = Nodes.get(i);
            //get the area of the node
            float[] pos = node.getNodeArea(node);
            //check if click was inside boundaries of node
            if(this.x > pos[0] && this.x < pos[2] && this.y > pos[1] && this.y < pos[3]){
                ret = true;
            }
            else{ ret = false;}
        }
        Log.i(TAG, "nodeClicked: "+ret);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // tap
                this.x = event.getX();
                this.y = event.getY();
                //Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);

                if(nodeClicked(event))
                {
                    Toast.makeText(context, "node clicked", Toast.LENGTH_SHORT).show();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE: // drag
                this.x = event.getX();
                this.y = event.getY();
                Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);
//                if(nodeClicked(company)) {
//                    company.nodeSetX(event.getX());
//                    company.nodeSetY(event.getY());
//                    company.drawNode(mCanvas, company);
//                }
                invalidate();
                break;
        }


        this.postInvalidate();
        return true;
    }
}
