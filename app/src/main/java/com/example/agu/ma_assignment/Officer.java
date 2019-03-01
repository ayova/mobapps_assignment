package com.example.agu.ma_assignment;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Officer {

    public Officer(String officerName, String officerAddress, String officerNationality, String officerDoB) {
        OfficerName = officerName;
        OfficerAddress = officerAddress;
        OfficerNationality = officerNationality;
        OfficerDoB = officerDoB;
    }

    @PrimaryKey (autoGenerate = true)
    private int id;
    @ColumnInfo (name = "officer_name")
    private String OfficerName;
    @ColumnInfo (name = "officer_address")
    private String OfficerAddress;
    @ColumnInfo (name = "officer_nationality")
    private String OfficerNationality;
    @ColumnInfo (name = "officer_dob")
    private String OfficerDoB;


    //getters for officer class
    public int getId() { return id; }
    public String getOfficerName() { return OfficerName; }
    public String getOfficerAddress() { return OfficerAddress; }
    public String getOfficerNationality() { return OfficerNationality; }
    public String getOfficerDoB() { return OfficerDoB; }
}
