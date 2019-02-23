package com.example.agu.ma_assignment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class NodeArcGenerator extends View {

    private float x;
    private float y;
    private ArrayList<Node> Nodes = new ArrayList<>();
    private Node company = new Node(500,500);
    private Canvas mCanvas;

    public NodeArcGenerator(Context context) {
        super(context);
        this.x = 0;
        this.y = 0;
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
        Log.i("node position", "onDraw: " + " x: "+company.nodeGetX() + " y: " + company.nodeGetY());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: // tap
                this.x = event.getX();
                this.y = event.getY();
                Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE: // drag
                this.x = event.getX();
                this.y = event.getY();
                Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);
                company.nodeSetX(event.getX());
                company.nodeSetY(event.getY());
                company.drawNode(mCanvas, company);
                invalidate();
                break;
        }


        this.postInvalidate();
        return true;
    }
}
