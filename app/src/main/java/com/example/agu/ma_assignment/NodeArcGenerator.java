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
    private Canvas mCanvas;
    Paint generic = new Paint();
    private Node company = new Node();

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

            //scatter officer nodes around the company node
            int j=0;
            for (int i = 0; i < 360; i += angleIncrease) {
                float angle = (float) Math.toRadians(i); // Need to convert to radians first

                float ofNodeX = (float) (cx + radius * Math.sin(angle));
                float ofNodeY = (float) (cy - radius * Math.cos(angle));

                Node ofNode = new Node(j, ofNodeX, ofNodeY, 25);
                ofNode.drawNode(canvas, ofNode);
                Nodes.add(ofNode);
                canvas.drawLine(company.nodeGetX(), company.nodeGetY(), ofNodeX, ofNodeY, generic);
                j++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
//        //set canvas to the middle of the screen
//        canvas.translate(getWidth()/2,getHeight()/2);
        //paint
        generic.setColor(Color.RED);

        company.nodeSetX(canvas.getWidth()/2);
        company.nodeSetY(canvas.getHeight()/2);
        company.nodeSetRadius(50);
        company.drawNode(canvas, company);
        drawOfficers(canvas);

    }

    //check if the click event hits a node on the canvas (i.e. node is pressed)
    public void nodeClicked(float x, float y){
        boolean clicked = false;
        Node nodeCld = new Node();
        for (int i = 0; i < Nodes.size(); i++) { //get area for each node
            float[] pos = Nodes.get(i).getNodeArea(Nodes.get(i)); //get the area of the node
//            Log.i(TAG, "nodeClicked: "+x+" "+pos[0]+" "+pos[2]+" "+y+" "+pos[1]+" "+pos[3]);
            Node node = Nodes.get(i);
            if(x > pos[0] && x < pos[2] && y > pos[1] && y < pos[3]){ //check if click was inside boundaries of node
                clicked = true;
                nodeCld = node;
            }
        }
        if (clicked == true){
            Toast.makeText(context, "Node clicked:"+nodeCld.getId(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // tap
                this.x = event.getX();
                this.y = event.getY();
                //Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y); //uncomment to see where the click took place
                nodeClicked(event.getX(),event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE: // drag
                this.x = event.getX();
                this.y = event.getY();
                //Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y); //uncomment to track finger on screen
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
