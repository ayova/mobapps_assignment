package com.example.agu.ma_assignment;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NodeArcGenerator extends View {

    final String TAG = "NodeArcGen";
    private ArrayList<Node> Nodes = new ArrayList<>();
    private Canvas mCanvas;
    Paint generic = new Paint();
    private Node company = new Node();
    int mCanWidth, mCanHeight;
    private final static float minZoom = 1.f;
    private final static float maxZoom =10.0f;
    private float scaleFactor = 1.f;
    private ScaleGestureDetector mScaleGestureDetector;
    private Context context;
    private List<Officer> ofNodeLs;
    private final static int NONE = 0;
    private final static int PAN = 1;
    private final static int ZOOM = 2;
    private int EventState;
    private float StartX = 0;
    private float StartY = 0;
    private float TranslatedX = 0;
    private float TranslatedY = 0;
    private float PreviousTranslatedX = 0;
    private float PreviousTranslatedY = 0;
    private float scaleStartX, scaleStartY;


    /* Used later to detect and respond to onTouchEvent(s) like panning and zooming */
    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(minZoom,Math.min(scaleFactor, maxZoom));
            // get the two points to execute the zooming
            scaleStartX = detector.getFocusX();
            scaleStartY = detector.getFocusY();

            invalidate();
            return super.onScale(detector);
        }
    }

    /* Basic constructor */
    public NodeArcGenerator(Context context) {
        super(context);
        this.context = context;
        float x = 0;
        float y = 0;
        mScaleGestureDetector = new ScaleGestureDetector(getContext(),new ScaleListener());
    }

    /* after the wait thread is executed, then call redraw to make the diagram visible */
    public void redraw(){ invalidate(); }

    /*draw the officer nodes around the company node that's in the very center */
    private void drawOfficers(Canvas canvas){
        //connection with rooms database to retrieve officers data
        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
        ofNodeLs = db.officerDao().getAllOfficers();
        Paint generic = new Paint();
        generic.setColor(Color.BLACK);
        Log.d(TAG, "drawOfficers: count >> "+db.officerDao().getOfficerCount());
        if(ofNodeLs.isEmpty()){
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
        }
        else{
            int angleIncrease = 360 / ofNodeLs.size(); //divide the circumference in as many angles as officers there are
            float cx = company.nodeGetX();
            float cy = company.nodeGetY();
            float radius = 500;

            //scatter officer nodes around the company node
            int j=0;
            for (int i = 0; i < 360; i += angleIncrease) {
                float angle = (float) Math.toRadians(i); // Need to convert to radians first

                // trigonometry for setting officers around company nodes
                float ofNodeX = (float) (cx + radius * Math.sin(angle));
                float ofNodeY = (float) (cy - radius * Math.cos(angle));

                Node ofNode = new Node(j, ofNodeX, ofNodeY, 25); //use j to index all nodes differently so they are unique
                ofNode.drawNode(canvas, ofNode, "officer"); //draw the node with "officer" paint (blue)
                Nodes.add(ofNode); //add the newly generated node to the arraylist
                canvas.drawLine(company.nodeGetX(), company.nodeGetY(), ofNodeX, ofNodeY, generic); //line to connect each officer node with company node
                j++;
            }
        }
        db.close(); //close connection to avoid memory leaks
    }

    /* Where all items are actually drawn to the canvas */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale(scaleFactor,scaleFactor);
        // detect pinch x,y
        if((TranslatedX * -1) < 0) {
            TranslatedX = 0;
        } else if ((TranslatedX * -1) > canvas.getWidth() * scaleFactor - getWidth()) {
            TranslatedX = (canvas.getWidth() * scaleFactor - getWidth()) * -1;
        }
        if((TranslatedY * -1) < 0) {
            TranslatedY = 0;
        } else if ((TranslatedY * -1) > canvas.getHeight() * scaleFactor - getHeight()) {
            TranslatedY = (canvas.getHeight() * scaleFactor - getHeight()) * -1;
        }
        canvas.translate(TranslatedX/scaleFactor, TranslatedY/scaleFactor);
        generic.setColor(Color.RED);
        centerComp();
        company.nodeSetRadius(50);
        company.drawNode(canvas, company);
        drawOfficers(canvas);

        canvas.restore();
        mCanvas = canvas;
    }

    /* Used to center the company (whenever coords hit 0,0 i.e. at the very start).
     * Mainly used for centering company when first drawing to the canvas */
    public void centerComp(){
        if (company.nodeGetX() == 0 && company.nodeGetY() == 0 || scaleFactor == 1.f){
            company.nodeSetX(getWidth()/2);
            company.nodeSetY(getHeight()/2);
        }
    }

    /* Check if the click event hits a node on the canvas (i.e. node is pressed) */
    public void nodeClicked(float x, float y){
        boolean clicked = false; // false by default
        Node nodeCld = new Node(); //node var to hold the node that's being clicked on
        for (int i = 0; i < Nodes.size(); i++) {                        //for each node
            float[] pos = Nodes.get(i).getNodeArea(Nodes.get(i));       //get the area of the node
            Node node = Nodes.get(i);                                   //get the node that's being clicked on
            if(x > pos[0] && x < pos[2] && y > pos[1] && y < pos[3]){   //check if click was inside boundaries of node
                clicked = true;
                nodeCld = node;                                         //and pass it outside the loop
            }
        }
        if (clicked){ //if an officer node was clicked, then... display message and proceed to officer info
            Toast.makeText(context, "Node "+nodeCld.getId()+" clicked.", Toast.LENGTH_SHORT).show(); //e.g. Officer 2 clicked
            displayOfficerData(nodeCld.getId()); //display officer data for the given node
        }
    }

    /* Function used to launch the activity in which officer information is displayed */
    private void displayOfficerData(int offiID){
        Intent intent = new Intent(context, DisplayOfficerData.class);
        intent.putExtra("offiID", offiID);
        context.startActivity(intent);
    }

    /* Used to detect user input such as tap, drag, etc. */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                EventState = PAN;
                StartX = event.getX() - PreviousTranslatedX; //work out click depending on translation of canvas
                StartY = event.getY() - PreviousTranslatedY;
                nodeClicked(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                EventState = NONE;
                PreviousTranslatedX = TranslatedX;
                PreviousTranslatedY = TranslatedY;
                break;
            case MotionEvent.ACTION_MOVE:
                TranslatedX = event.getX() - StartX; //notifies of where coordinates are currently situated
                TranslatedY = event.getY() - StartY;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                EventState = ZOOM;
                break;
        }
        mScaleGestureDetector.onTouchEvent(event);
        if((EventState == PAN && scaleFactor != minZoom) || EventState == ZOOM) {
            //when zooming or panning, redraw the canvas
            invalidate();       //change appearance
            requestLayout();    //change bounds
        }
        return true;


    }

    /* onMeasure overriden in order to resize the canvas when scaling up or down */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get canvas size
        mCanWidth = MeasureSpec.getSize(widthMeasureSpec);
        mCanHeight = MeasureSpec.getSize(heightMeasureSpec);

        //resize canvas as we scale
        int scaleWidth = Math.round(mCanWidth * scaleFactor);
        int scaleHeight = Math.round(mCanHeight * scaleFactor);
        setMeasuredDimension(Math.min(mCanWidth, scaleWidth),Math.min(mCanHeight,scaleHeight));

    }
}
