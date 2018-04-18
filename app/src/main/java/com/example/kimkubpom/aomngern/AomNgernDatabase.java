package com.example.kimkubpom.aomngern;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.kimkubpom.aomngern.DAO.UserDao;
import com.example.kimkubpom.aomngern.Entities.User;

/**
 * Created by kimkubpom on 18/4/2018 AD.
 */

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AomNgernDatabase extends RoomDatabase {

    private static AomNgernDatabase  INSTANCE;

    public abstract UserDao userDao();

    public static AomNgernDatabase  getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AomNgernDatabase .class, "AomNgern.db")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}