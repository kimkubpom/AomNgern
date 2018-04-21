// Kornkamol Chirasupakul 5888024
// Jidapa Kongsakoonwong 5888081
// Phanaphon Sereelertwiwat 5888157

// Main activity is the first page that will call the DatabaseInitializer and start other activity

package com.example.kimkubpom.aomngern;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kimkubpom.aomngern.Activity.LogInActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseInitializer.populateAsync(AomNgernDatabase.getDatabase(this));
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        AomNgernDatabase.destroyInstance();
        super.onDestroy();
    }

    private void populateDb() {
        DatabaseInitializer.populateSync(AomNgernDatabase.getDatabase(this));
    }
}
