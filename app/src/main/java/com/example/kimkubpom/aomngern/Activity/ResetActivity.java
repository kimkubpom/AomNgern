package com.example.kimkubpom.aomngern.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kimkubpom.aomngern.R;

public class ResetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
    }

    public static class MenuActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
        }
    }
}
