// Kornkamol Chirasupakul 5888024
// Jidapa Kongsakoonwong 5888081
// Phanaphon Sereelertwiwat 5888157

// Log in activity is the page allowing users to log in with existing account into the application or sign up new account


package com.example.kimkubpom.aomngern.Activity;

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






public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.password_input) EditText passwordInput;
    @BindView(R.id.sign_up) Button signUpButton;
    @BindView(R.id.log_in) Button logInButton;

    public String email;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    public void signUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    public void logIn(){
        if (!validate()) {
            onLoginFailed();
            return;
        }

        logInButton.setEnabled(false);


        this.email = emailInput.getText().toString();
        this.password = passwordInput.getText().toString();

        // Call thread to access DAO and check whether the email is taken or not
        new insertAsyncTask(this.email, this.password).execute();


    }

    private class insertAsyncTask extends AsyncTask<String, Void, Boolean> {

        private boolean exist;
        private String email;
        private String password;

        insertAsyncTask(String email, String password) {
            this.email = email;
            this.password = password;
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
//            progressDialog.dismiss();
            if(exist){
                Toast.makeText(getBaseContext(), "Log in successfully", Toast.LENGTH_LONG).show();
                onLoginSuccess();
            }
            else{
                Toast.makeText(getBaseContext(), "You have entered wrong email or password", Toast.LENGTH_LONG).show();
                //can set some interval
                logInButton.setEnabled(true);
            }
        }
    }

    public boolean validate() {

        boolean valid = true;

        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();


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

        return valid;
    }

    public void onLoginFailed() {

        Toast.makeText(getBaseContext(), "You have entered wrong email or password", Toast.LENGTH_LONG).show();
        logInButton.setEnabled(true);
    }

    public void onLoginSuccess() {

        logInButton.setEnabled(true);

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
