package com.example.agu.ma_assignment;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> CNames = new ArrayList<String>();
    private ArrayList<String> CNumber = new ArrayList<String>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> CNames,ArrayList<String> CNumber, Context mContext) {
        this.CNumber = CNumber;
        this.CNames = CNames;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_display,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        Log.i("CNames size", "onBindViewHolder: "+CNames.get(i));
        holder.recyclerTV.setText(CNames.get(i));
        holder.recycler_Temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onBind", "onClick: "+ CNames.get(i));
                Toast.makeText(mContext, "Clicked on: "+CNames.get(i), Toast.LENGTH_SHORT).show();
                //add functionality for the click on the company name here >>>
                // intent to open activity: nodearcgraph
                Intent intent = new Intent(mContext, NodeArcGraph.class);
                //putExtra to attach the data to be sent over to the other activity
                intent.putExtra("compName", CNames.get(i));
                intent.putExtra("compNumber", CNumber.get(i));
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return CNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView recyclerTV;
        ConstraintLayout recycler_Temp;


        public ViewHolder(View itemView) {
            super(itemView);
            recyclerTV = itemView.findViewById(R.id.tv_recycler_temp);
            recycler_Temp = itemView.findViewById(R.id.recycler_template_layout);

        }

    }
}
