package com.example.agu.ma_assignment;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity
public class Officer {

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


    public Officer() { /*empty constructor needed*/ }

    public Officer(String officerName, String officerAddress, String officerNationality, String officerDoB) {
        OfficerName = officerName;
        OfficerAddress = officerAddress;
        OfficerNationality = officerNationality;
        OfficerDoB = officerDoB;
    }



    //getters for officer class
    public int getId() { return id; }
    public String getOfficerName() { return OfficerName; }
    public String getOfficerAddress() { return OfficerAddress; }
    public String getOfficerNationality() { return OfficerNationality; }
    public String getOfficerDoB() { return OfficerDoB; }

    public void setId(int id) { this.id = id; }
    public void setOfficerName(String officerName) { OfficerName = officerName; }
    public void setOfficerAddress(String officerAddress) { OfficerAddress = officerAddress; }
    public void setOfficerNationality(String officerNationality) { OfficerNationality = officerNationality; }
    public void setOfficerDoB(String officerDoB) { OfficerDoB = officerDoB; }
}
