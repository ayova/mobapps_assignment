package com.example.agu.ma_assignment;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface OfficerDao {

    @Query("SELECT * FROM Officer")
    List<Officer> getAllOfficers();

    @Query("SELECT * FROM Officer")
    LiveData<List<Officer>> getAllOfficers1();

    @Query("DELETE FROM Officer")
    void deleteAllOfficers();

    @Query("SELECT COUNT(id) FROM Officer")
    int getOfficerCount();

    @Insert
    void insertAllOfficers(Officer... officers);

    @Insert
    void insertOfficer(Officer officer);

}
