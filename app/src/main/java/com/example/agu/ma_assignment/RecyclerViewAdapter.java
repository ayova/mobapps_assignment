package com.example.agu.ma_assignment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

//public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private static final String TAG = "RecyclerViewAdapter";
//
//    //data passing
//    private ArrayList<String> compNames = new ArrayList<>();
//    private ArrayList<String> compNo = new ArrayList<>();
//    private Context mContext;
//
//    public RecyclerViewAdapter(Context mContext, ArrayList<String> compNames, ArrayList<String> compNo) {
//        this.compNames = compNames;
//        this.compNo = compNo;
//        this.mContext = mContext;
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list, parent, false);
//        viewHolder holder = new viewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//        holder.tvComp.setText(CompNames.getText(position));
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class viewHolder extends RecyclerView.ViewHolder {
//
//        //vars
//        TextView tvComp;
//        RelativeLayout relativeLayoutRecycler;
//
//        public viewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvComp = itemView.findViewById(R.id.tv_comp);
//            relativeLayoutRecycler = itemView.findViewById(R.id.relativeLayoutRecycler);
//
//        }
//    }
//}

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.recHolder> {
    private ArrayList<String> mDataset = new ArrayList<>();
    RelativeLayout relRec;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class recHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public recHolder(View v) {
            super(v);
            relRec = v.findViewById(R.id.relativeLayoutRecycler);
            mTextView = v.findViewById(R.id.tv_comp);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public RecyclerViewAdapter(ArrayList<String> mDataset, RelativeLayout relRec, Context mContext) {
        this.mDataset = mDataset;
        this.relRec = relRec;
        this.mContext = mContext;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public recHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try {
            // create a new view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list, parent, false);
            Log.d("onViewHolder", "onCreateViewHolder: view created");
            //viewholder object to hold the view to be added
            recHolder rh = new recHolder(view);
            return rh;
        }
        catch (Exception e){
            Log.e("onViewHolder", "onCreateViewHolder: "+e );
            recHolder rh = new recHolder(null);
            return rh;
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull recHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        while (position <= mDataset.size()) {
            holder.mTextView.setText(mDataset.get(position));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}