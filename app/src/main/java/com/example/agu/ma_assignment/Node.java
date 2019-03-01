package com.example.agu.ma_assignment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Node extends Canvas {

    // TODO: 01/03/2019 give nodes id so they are unique and correspond to one officer
    private float x=0;
    private float y=0;
    private int radius;
    public Node(float x, float y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
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

    public int getNodeRadius(){return this.radius;}

    public void drawNode(Canvas canvas, Node node){
        Paint generic = new Paint();
        generic.setColor(Color.RED);
        canvas.drawCircle(node.nodeGetX(),node.nodeGetY(),50, generic);
    }

    public float[] getNodeArea(Node node){
        //calculate the borders where to register the clicks
        float left = node.nodeGetX() - node.getNodeRadius();
        float top = node.nodeGetY() - node.getNodeRadius();
        float right = node.nodeGetX() + node.getNodeRadius();
        float bottom = node.nodeGetY() + node.getNodeRadius();
        //Log.i("Area: ", "getNodeArea: "+left+" "+top+" "+right+" "+bottom);
        //save them in the array to be returned
        float[] pos;
        // [0] - left, [1] - top, [2] - right, [3] - bottom
        pos = new float[]{left,top,right,bottom};
        //return the array to be used for click detection
        return  pos;
    }
}
