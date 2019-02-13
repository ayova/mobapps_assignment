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
