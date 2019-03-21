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
    private float x;
    private float y;
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
    int mCanvasWidth, mCanvasHeight;
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



    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(minZoom,Math.min(scaleFactor, maxZoom));
            scaleStartX = detector.getFocusX();
            scaleStartY = detector.getFocusY();

            invalidate();
            //requestLayout();
            return super.onScale(detector);
        }
    }

    public NodeArcGenerator(Context context) {
        super(context);
        this.context = context;
        this.x = 0;
        this.y = 0;
        mScaleGestureDetector = new ScaleGestureDetector(getContext(),new ScaleListener());
    }

    //draw the officer nodes around the company node that's in the very center
    private void drawOfficers(Canvas canvas){
        //connection with rooms database to retrieve officers data
        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
        ofNodeLs = db.officerDao().getAllOfficers();
        Paint generic = new Paint();
        generic.setColor(Color.BLACK);
        Log.d(TAG, "drawOfficers: count >> "+db.officerDao().getOfficerCount());
        if(ofNodeLs.isEmpty()){
            Toast.makeText(context, "No directors available", Toast.LENGTH_SHORT).show();
        }
        else {
            int angleIncrease = 360 / ofNodeLs.size(); //divide the circumference in as many angles as officers there are
            float cx = company.nodeGetX();
            float cy = company.nodeGetY();
            float radius = 500;

            //scatter officer nodes around the company node
            int j=0;
            for (int i = 0; i < 360; i += angleIncrease) {
                float angle = (float) Math.toRadians(i); // Need to convert to radians first

                float ofNodeX = (float) (cx + radius * Math.sin(angle));
                float ofNodeY = (float) (cy - radius * Math.cos(angle));

                Node ofNode = new Node(j, ofNodeX, ofNodeY, 25);
                ofNode.drawNode(canvas, ofNode, "officer");
                Nodes.add(ofNode);
                canvas.drawLine(company.nodeGetX(), company.nodeGetY(), ofNodeX, ofNodeY, generic);
                j++;
            }
        }
        db.close();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale(scaleFactor,scaleFactor, scaleStartX, scaleStartY); // detect pinch x,y
//        canvas.translate(TranslatedX/scaleFactor, TranslatedY/scaleFactor);
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

    //used to center the company (whenever coords hit 0,0)
    public void centerComp(){
        if (company.nodeGetX() == 0 && company.nodeGetY() == 0 || scaleFactor == 1.f){
            company.nodeSetX(getWidth()/2);
            company.nodeSetY(getHeight()/2);
        }
    }

    //check if the click event hits a node on the canvas (i.e. node is pressed)
    public void nodeClicked(float x, float y){
        boolean clicked = false;
        Node nodeCld = new Node(); //node var to hold the node that's being clicked on
        for (int i = 0; i < Nodes.size(); i++) { //get area for each node
            float[] pos = Nodes.get(i).getNodeArea(Nodes.get(i)); //get the area of the node
            Node node = Nodes.get(i); //get the node that's being clicked on
            if(x > pos[0] && x < pos[2] && y > pos[1] && y < pos[3]){ //check if click was inside boundaries of node
                clicked = true;
                nodeCld = node;//and pass it outside the loop
            }
        }
        if (clicked == true){ //if an officer node was clicked, then...
            // TODO: 07/03/2019 A new recycler view should display the officer information
            Toast.makeText(context, "Node clicked:"+nodeCld.getId(), Toast.LENGTH_SHORT).show();
            displayOfficerData(nodeCld.getId());
        }
    }

    //use to drag the entire node graph around
    public boolean companyDragged(float x, float y,Node company){
        float[] pos = company.getNodeArea(company);
        if(x > pos[0] && x < pos[2] && y > pos[1] && y < pos[3]) { //check if click was inside boundaries of the company node
            return true;
        }
        else{ return false;}

        }

    private void displayOfficerData(int offiID){
        Intent intent = new Intent(context, DisplayOfficerData.class);
        intent.putExtra("offiID", offiID);
        context.startActivity(intent);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//        switch(event.getAction()){
//            case MotionEvent.ACTION_DOWN: // tap
//                this.x = event.getX();
//                this.y = event.getY();
//                //Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y); //uncomment to see where the click took place
//                nodeClicked(event.getX(),event.getY());
//                invalidate();
//                break;
//            case MotionEvent.ACTION_MOVE: // drag
//                this.x = event.getX();
//                this.y = event.getY();
//                //Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y); //uncomment to track finger on screen
//                if(companyDragged(event.getX(),event.getY(),company)) {
//                    company.nodeSetX(event.getX());
//                    company.nodeSetY(event.getY());
//                    company.drawNode(mCanvas, company);
//                }
//                invalidate();
//                break;
//        }
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                EventState = PAN;
                StartX = event.getX() - PreviousTranslatedX;
                StartY = event.getY() - PreviousTranslatedY;
                nodeClicked(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                EventState = NONE;
                PreviousTranslatedX = TranslatedX;
                PreviousTranslatedY = TranslatedY;
                break;
            case MotionEvent.ACTION_MOVE:
                TranslatedX = event.getX() - StartX;
                TranslatedY = event.getY() - StartY;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                EventState = ZOOM;
                break;
        }
        mScaleGestureDetector.onTouchEvent(event);
        if((EventState == PAN && scaleFactor != minZoom) || EventState == ZOOM) {
            invalidate();
            requestLayout();
        }
        return true;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get canvas size
        mCanWidth = MeasureSpec.getSize(widthMeasureSpec);
        mCanHeight = MeasureSpec.getSize(heightMeasureSpec);

//        mCanvasWidth = mCanvas.getWidth();
//        mCanHeight = mCanvas.getHeight();
//        //resize canvas as we scale

        // TODO: 08/03/2019 need to get the code below working for it to zoom and pan properly; problem being: mCanvas = null 
        int scaleWidth = Math.round(mCanvasWidth * scaleFactor);
        int scaleHeight = Math.round(mCanvasHeight * scaleFactor);
//        setMeasuredDimension(Math.min(mCanWidth, scaleWidth),Math.min(mCanHeight,scaleHeight));

        int desiredWidth = 500;
        int desiredHeight = 500;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }
}
