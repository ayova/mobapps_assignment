package com.example.agu.ma_assignment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NodeArcGenerator extends View {

    private float x;
    private float y;



    public NodeArcGenerator(Context context) {
        super(context);
        this.x = 0;
        this.y = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect mrec = new Rect();
        mrec.set(0,0,canvas.getWidth(),canvas.getHeight());

        Paint bgCol = new Paint();
        bgCol.setColor(Color.GREEN);
        bgCol.setStyle(Paint.Style.FILL);

        canvas.drawRect(mrec,bgCol);

        //draw company node in the middle
        canvas.translate(getWidth()/2,getHeight()/2);
        Paint cir = new Paint();
        Node circle = new Node(getWidth(),getHeight(),50);
        cir.setColor(Color.BLUE);
        canvas.drawCircle(0,0,50,cir);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            this.x = event.getX();
            this.y = event.getY();
            Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);
        }
        this.postInvalidate();
        return true;
    }
}
