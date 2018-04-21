// Kornkamol Chirasupakul 5888024
// Jidapa Kongsakoonwong 5888081
// Phanaphon Sereelertwiwat 5888157

// Sign up activity is the page allowing users to create new account by inputting all of the required information in the field
// This also checks whether the account is already existed in the database or not

package com.example.kimkubpom.aomngern.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kimkubpom.aomngern.AomNgernDatabase;
import com.example.kimkubpom.aomngern.Entities.User;
import com.example.kimkubpom.aomngern.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.email_signup) EditText emailInput;
    @BindView(R.id.password_signup) EditText passwordInput;
    @BindView(R.id.name_signup) EditText nameInput;
    @BindView(R.id.phone_signup) EditText phoneInput;
    @BindView(R.id.create_account) Button createAccButton;

    public String email;
    public String password;
    public String name;
    public String phone;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this, R.style.AppTheme);

//        createAccButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createAccount(v);
//            }
//        });
    }

    public void createAccount(View view){
        if (!validate()) {
            onSignupFailed();
            return;
        }

        createAccButton.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating an account...");
        progressDialog.show();

        this.email = emailInput.getText().toString();
        this.password = passwordInput.getText().toString();
        this.name = nameInput.getText().toString();
        this.phone = phoneInput.getText().toString();

        // Call thread to access DAO and check whether the email is taken or not
        new insertAsyncTask(this.email, this.password, this.name, this.phone).execute();

    }

    private class insertAsyncTask extends AsyncTask<String, Void, Boolean> {

        private boolean exist;
        private String email;
        private String password;
        private String name;
        private String phone;

        insertAsyncTask(String email, String password, String name, String phone) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.phone = phone;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            User temp = AomNgernDatabase.getDatabase(getApplicationContext()).userDao().getUserbyEmail(email);
            if(temp == null)
                exist = false;
            else
                exist = true;
            return exist;
        }

        @Override
        protected void onPostExecute(Boolean exist) {
            progressDialog.dismiss();
            if(!exist){
                Toast.makeText(getBaseContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                onSignupSuccess();
            }
            else{
                Toast.makeText(getBaseContext(), "Email already exist", Toast.LENGTH_LONG).show();
                //can set some interval
                createAccButton.setEnabled(true);
            }
        }
    }

    public boolean validate() {

        boolean valid = true;

        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        name = nameInput.getText().toString();
        phone = phoneInput.getText().toString();

        // Check email address by regex ...@....
        if(email.isEmpty() || email.length() <3) {
            emailInput.setError("input a valid email address");
            valid = false;
        } else {
            emailInput.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordInput.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        if (name.isEmpty() || name.length() < 3) {
            nameInput.setError("at least 3 characters");
            valid = false;
        } else {
            nameInput.setError(null);
        }

        // Check if it's only number

        if (phone.isEmpty()) {
            phoneInput.setError("input only number");
            valid = false;
        } else {
            phoneInput.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {

        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        createAccButton.setEnabled(true);
    }

    public void onSignupSuccess() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(email, password, name, phone);
                AomNgernDatabase.getDatabase(getApplicationContext()).userDao().addUser(user);
            }
        }).start();

        createAccButton.setEnabled(true);

        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
        finish();

    }
}