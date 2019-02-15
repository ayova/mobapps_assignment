package com.example.agu.ma_assignment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.View;

public class Node extends Canvas {

    private float x=0;
    private float y=0;
    private int radius=0;
    public Node(float x, float y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
}
