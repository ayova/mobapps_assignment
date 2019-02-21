package com.example.agu.ma_assignment;

import android.graphics.Canvas;

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

}
