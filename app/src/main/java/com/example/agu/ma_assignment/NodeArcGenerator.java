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
    private Node company;



    public NodeArcGenerator(Context context) {
        super(context);
        this.x = 0;
        this.y = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float canvx, canvy;
        canvx = getWidth()/2;
        canvy = getHeight()/2;
        //draw company node in the middle
        canvas.translate(canvx,canvy);
        Paint cir = new Paint();
        company = new Node(canvx,canvy);
        cir.setColor(Color.BLUE);
        canvas.drawCircle(0,0,50,cir);
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
                if(this.x == company.nodeGetX() && this.y == company.nodeGetY()){
                    Log.d("WOORKSSSS", "onTouchEvent: DRAGGGGGG ");
                }
                Log.d("LISTENEDEVENT", "X:"+x+" Y:"+y);
                invalidate();
                break;
        }


        this.postInvalidate();
        return true;
    }
}
