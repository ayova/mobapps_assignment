package com.example.agu.ma_assignment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.Color.RED;

public class Node extends Canvas {

    private float x=0;
    private float y=0;
    public Node(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float nodeGetX(){
        return this.x;
    }
    public float nodeGetY(){
        return this.y;
    }

    public void nodeSetX(float x){
        this.x = x;
    }
    public void nodeSetY(float y){
        this.y = y;
    }

    public void drawNode(Canvas canvas, Node node){
        Paint generic = new Paint();
        generic.setColor(Color.RED);
        canvas.drawCircle(node.nodeGetX(),node.nodeGetY(),50, generic);

    }
}
