package com.example.agu.ma_assignment;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> CNames = new ArrayList<String>();
    private ArrayList<String> CNumber = new ArrayList<String>();
    private Context mContext;
    private SearchActivity search;

    private String CName;
    private String Cnumber;

    private ArrayList<String> officerName = new ArrayList<>();
    private ArrayList<String> officerDoB = new ArrayList<>();
    private ArrayList<String> officerNat = new ArrayList<>();
    private ArrayList<String> officerAddress = new ArrayList<>();



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
                //Toast.makeText(mContext, "Clicked on: "+CNames.get(i), Toast.LENGTH_SHORT).show(); // debugging only

                api_search_officers(v, CNumber.get(i));

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

    private void api_search_officers(View view, String CNumber){
        //Create request queue
        RequestQueue rQueue = Volley.newRequestQueue(this.mContext);
        //JSONRequest
        //search url for company names
        Cnumber = CNumber;
        final String searchURL = "https://api.companieshouse.gov.uk/company/" + Cnumber + "/officers?register_type=directors";
        JsonObjectRequest JSONretriever = new JsonObjectRequest(Request.Method.GET, searchURL, null,
                new Response.Listener <JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) { //what to do with the response from the API

                        try {
                            if(response.getJSONArray("items").length() == 0){
                                Toast.makeText(RecyclerViewAdapter.this.mContext, "No officers available for this company.", Toast.LENGTH_SHORT).show();
                            }
                            JSONArray arr = response.getJSONArray("items");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject o = arr.getJSONObject(i);
                                //add officer data to relevant array
                                String name = o.getString("name");
                                officerName.add(name);
                                Log.i("logging data", "onResponse: " + name);

                                //officer's nationality
                                String nationality;
                                if(!o.has("nationality")){
                                    nationality = "Unavailable";
                                    officerNat.add(nationality);
                                }
                                else{
                                    nationality = o.getString("nationality");
                                    officerNat.add(nationality);
                                }
                                Log.i("logging data", "onResponse: " + nationality);

                                //officer's year of birth
                                JSONObject dob = o.getJSONObject("date_of_birth");
                                String yr;
                                if(!dob.has("year")){
                                    yr = "Unavailable";
                                    officerDoB.add(yr);
                                }
                                else {
                                    yr = dob.getString("year");
                                    officerDoB.add(yr);
                                }
                                Log.i("logging data", "onResponse: " + yr);

                                //officer's address
                                JSONObject addr = o.getJSONObject("address");
                                String postCode, cty, stNo, street, ctry, ofAddress;
                                if(!addr.has("postal_code") || !addr.has("locality") || !addr.has("premises") || !addr.has("address_line_1") || !addr.has("country")){
                                    ofAddress = "Unavailable";
                                    officerAddress.add(ofAddress);

                                }
                                else {
                                    postCode = addr.getString("postal_code");
                                    cty = addr.getString("locality");
                                    stNo = addr.getString("premises");
                                    street = addr.getString("address_line_1");
                                    ctry = addr.getString("country");
                                    ofAddress = stNo + " " + street + ", " + cty + ", " + postCode + ". " + ctry;
                                    officerAddress.add(ofAddress);
                                }

                                for(int j=0; j< officerName.size(); j++){
                                    Log.i("Officer "+j, "Details: "+officerName.get(j)+" ,"+officerDoB.get(j)+" ,"+officerNat.get(j)+" ,"+officerAddress.get(j));
                                }

                            }
                        } catch (Exception e) { //if the response generates an exception
                            Log.e("Response exception", "onResponse: ",e );
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // if anything goes wrong... Print error to log
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        String code = networkResponse.toString();
                        if(code == "404"){
                            Log.e("JSON", "JSON error: " + networkResponse.statusCode);
                            Toast.makeText(RecyclerViewAdapter.this.mContext, "No officers available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map< String, String > getHeaders() throws AuthFailureError { // set the header for authentication (aka send API key)
                HashMap headers = new HashMap();
                headers.put("Authorization", "sic7yu4YhmpLToiLXPT7bGS_QvXW8SjIPLuhIfoF");
                return headers;
            }
        };
        //Finally, add the request to the request queue
        rQueue.add(JSONretriever);
    }

}
