package com.example.agu.ma_assignment;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> CNames;
    private ArrayList<String> CNumber;
    private Context mContext;
    private SearchActivity search;
    private String CName;
    private String Cnumber;
    private Intent intent;
    List<Officer> officers =  new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<String> CNames,ArrayList<String> CNumber, Context mContext) {
        this.CNumber = CNumber;
        this.CNames = CNames;
        this.mContext = mContext;

    }

    /* Function used to populate the view based on a template */
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        //with recycler_layout_display being the template
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
                //show on log which company is selected/clicked on
                Log.d("onBind", "onClick: "+ CNames.get(i));

                /* intent to open activity: nodearcgraph and
                 * pass it both the company name and number */
                intent = new Intent(mContext, NodeArcGraph.class);
                //putExtra to attach the data to be sent over to the other activity
                intent.putExtra("compName", CNames.get(i));
                intent.putExtra("compNumber", CNumber.get(i));
                api_search_officers(v, CNumber.get(i), CNames.get(i)); //gets right information but doesnt save to local variables (ArrayList<String>

                AppDatabase db = Room.databaseBuilder(mContext,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
                LiveData<List<Officer>> a = db.officerDao().getAllOfficers1();
                a.observeForever((List<Officer> listyboi)->{
                    mContext.startActivity(intent);
                });
                db.close(); //close db connection to avoid memory leaks
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

    /* Delete the officers that may remain in the database from an
     * older search. Use this before adding new officers to the db */
    private void deleteOldOfficers(){
        AppDatabase db = Room.databaseBuilder(mContext,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
        db.officerDao().deleteAllOfficers();
        db.close();
    }

    /* Function fit for retrieving all officers from the selected company
     * and storing the relevant details of each officer within that company,
     * including name, address, nationality, and date of birth. */
    private void api_search_officers(View view, String CNumber, final String CName){
        //first of all delete old officers from the database
        deleteOldOfficers();
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
                            //connect with the rooms db
                            AppDatabase db = Room.databaseBuilder(mContext,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
                            if(response.getJSONArray("items").length() == 0){
                                Toast.makeText(RecyclerViewAdapter.this.mContext, "No officers available for this company.", Toast.LENGTH_SHORT).show();
                            }
                            JSONArray arr = response.getJSONArray("items");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject o = arr.getJSONObject(i);
                                //temporal string holders
                                String name;
                                String nationality;
                                String ofAddress;
                                String ofDoB;
                                String yr, month; //holders that make up ofDoB
                                String postCode, cty, stNo, street, ctry; //holders that make up ofAddress

                                /* gather all credentials about directors
                                *  and check for empty items, like missing
                                *  names or addresses for example */
                                if(!o.has("name")){
                                    name = "Unavailable";
                                }
                                else{
                                    name = o.getString("name");
                                }

                                if(!o.has("nationality")){
                                    nationality = "Unavailable";
                                }
                                else{
                                    nationality = o.getString("nationality");
                                }

                                if(!o.has("date_of_birth")){
                                    ofDoB = "Unavailable";
                                }
                                else{
                                    JSONObject dob = o.getJSONObject("date_of_birth");

                                    if(!dob.has("year")){
                                        yr = "Unavailable";
                                    }
                                    else {
                                        yr = dob.getString("year");
                                    }
                                    if(!dob.has("month")){
                                        month = "Unavailable";
                                    }
                                    else {
                                        month = dob.getString("month");
                                    }
                                    ofDoB = month + " " + yr;
                                }
                                JSONObject addr = o.getJSONObject("address");
                                if(!addr.has("postal_code") || !addr.has("locality") || !addr.has("premises") || !addr.has("address_line_1") || !addr.has("country")){
                                    ofAddress = "Unavailable";
                                }
                                else {
                                    /* addr contains each item within "address" from the api call
                                     * use getString(string) to retrieve each desired item */
                                    postCode = addr.getString("postal_code");
                                    cty = addr.getString("locality");
                                    stNo = addr.getString("premises");
                                    street = addr.getString("address_line_1");
                                    ctry = addr.getString("country");
                                    ofAddress = stNo + " " + street + ", " + cty + ", " + postCode + ". " + ctry;
                                }
                                //create the new officer object and populate it with the right information
                                Officer insertOffi = new Officer();
                                insertOffi.setOfficerNationality(nationality);
                                insertOffi.setOfficerAddress(ofAddress);
                                insertOffi.setOfficerDoB(ofDoB);
                                insertOffi.setOfficerName(name);
                                //then insert this new officer into the officer database
                                db.officerDao().insertOfficer(insertOffi);
                            }
                            db.close();
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
                        if(code == "404" || code == "400" || code == "401"){
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
