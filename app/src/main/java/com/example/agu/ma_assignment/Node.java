package com.example.agu.ma_assignment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Node extends Canvas {

    private float x=0;
    private float y=0;
    private int radius;
    private int id;

    public Node(int id, float x, float y, int radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    public Node(){
        this.x = 0;
        this.y = 0;
        this.radius = 0;
    }
    public Node(int id){this.id = id;}

    public float nodeGetX(){
        return this.x;
    }
    public float nodeGetY(){
        return this.y;
    }

    //getters and setters for node
    public void nodeSetX(float x){
        this.x = x;
    }
    public void nodeSetY(float y){
        this.y = y;
    }
    public void nodeSetRadius(int radius){
        this.radius = radius;
    }
    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public int getNodeRadius(){return this.radius;}


    /* Draw the given node in the specified canvas */
    public void drawNode(Canvas canvas, Node node){
        //node and canvas are passed when calling the function
        Paint generic = new Paint();
        generic.setColor(Color.RED);
        canvas.drawCircle(node.nodeGetX(),node.nodeGetY(),50, generic);
    }

    /* Different instance of the same function where it may also contain
     * a string that determines "who" the node is. Useful for differentiating
     * between node types and applying different styles. */
    public void drawNode(Canvas canvas, Node node, String who){
        Paint generic = new Paint();
        if (who == "officer" ){
            generic.setColor(Color.BLUE);
        }
        canvas.drawCircle(node.nodeGetX(),node.nodeGetY(),50, generic);
    }

    /* Function that retrieves the top,left,right,bottom coordinates of a node*/
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
