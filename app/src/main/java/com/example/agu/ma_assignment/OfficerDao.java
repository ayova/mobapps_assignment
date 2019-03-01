package com.example.agu.ma_assignment;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

public interface OfficerDao {

    @Query("SELECT * FROM Officer")
    ArrayList<Officer> getAllOfficers();

    @Query("SELECT COUNT(id) FROM Officer")
    int getOfficerCount();

    @Insert()
    void insertAllOfficers(Officer... officers);
}
