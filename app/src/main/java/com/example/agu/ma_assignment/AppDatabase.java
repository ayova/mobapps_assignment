package com.example.agu.ma_assignment;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database( entities = {Officer.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OfficerDao officerDao();



}
