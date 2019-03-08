package com.example.agu.ma_assignment;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayOfficerData extends AppCompatActivity {

    private static final String TAG = "DisplayOfficerData";
    
    TextView officerName, officerAddress, officerDob, officerNationality; // textviews where to display officer data
    int officerId; //passed through intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_officer_data);
        //identify textviews in the layout
        officerAddress = findViewById(R.id.tv_offi_address);
        officerDob = findViewById(R.id.tv_offi_dob);
        officerName = findViewById(R.id.tv_offi_name);
        officerNationality = findViewById(R.id.tv_offi_nationality);
        officerId = getIntentData();
        displayOfficer(officerId);

    }
    private int getIntentData(){
        try{
            Intent intent = getIntent();
            if(intent.hasExtra("offiID")){
                officerId = getIntent().getIntExtra("offiID", 0); //default 0 in case no offiID was passed in the intent
            }
        }
        catch (Exception e){
            Log.e(TAG, "getIntentData: ",e );
            Toast.makeText(this, "unavailable", Toast.LENGTH_SHORT).show();
        }
        return officerId;
    }
    private void displayOfficer(int officerId){
        AppDatabase db = Room.databaseBuilder(this,AppDatabase.class,"officerDB").allowMainThreadQueries().build();
        Officer offi = db.officerDao().getAllOfficers().get(officerId);
        String txname, txdob,txnat, txaddr;
        txname = offi.getOfficerName();
        txdob = offi.getOfficerDoB();
        txnat = offi.getOfficerNationality();
        txaddr = offi.getOfficerAddress();
        Log.i(TAG, "displayOfficer: "+offi.getOfficerName()+offi.getOfficerAddress()+offi.getOfficerDoB()+offi.getOfficerNationality());
        officerAddress.setText(txname);
        officerDob.setText(txdob);
        officerNationality.setText(txnat);
        officerName.setText(txaddr);


        db.close();
    }
}
