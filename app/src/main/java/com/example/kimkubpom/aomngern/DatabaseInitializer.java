// Kornkamol Chirasupakul 5888024
// Jidapa Kongsakoonwong 5888081
// Phanaphon Sereelertwiwat 5888157

// DatabaseInitializer is the page that initialize the Room database in order to connect the AomNgernDatabase to the application

package com.example.kimkubpom.aomngern;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.kimkubpom.aomngern.Entities.User;

/**
 * Created by kimkubpom on 18/4/2018 AD.
 */

public class DatabaseInitializer {

    private Context context;
    private static final int DELAY_MILLIS = 500;

    public DatabaseInitializer(Context context){
        this.context = context;
    }

    public static void populateAsync(final AomNgernDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    // Initialize the database
    private static void populateWithTestData(AomNgernDatabase db) {
        db.userDao().deleteAll();

        // Add admin account
        addUser(db, "admin@123.com", "1234", "admin", "0888888888", "THB");
    }

    private static void addUser(final AomNgernDatabase db, final String email,
                                final String password, final String name, final String phone, final String currency) {
        User user = new User(email, password, name, phone, currency);
        db.userDao().addUser(user);
    }

    public static void populateSync(@NonNull final AomNgernDatabase database) {
        populateWithTestData(database);
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AomNgernDatabase mDb;

        PopulateDbAsync(AomNgernDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }


}