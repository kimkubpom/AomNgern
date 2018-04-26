// Kornkamol Chirasupakul 5888024
// Jidapa Kongsakoonwong 5888081
// Phanaphon Sereelertwiwat 5888157

// Sign up activity is the page allowing users to create new account by inputting all of the required information in the field
// This also checks whether the account is already existed in the database or not

package com.example.kimkubpom.aomngern.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kimkubpom.aomngern.AomNgernDatabase;
import com.example.kimkubpom.aomngern.Entities.User;
import com.example.kimkubpom.aomngern.MainActivity;
import com.example.kimkubpom.aomngern.R;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.email_signup) EditText emailInput;
    @BindView(R.id.password_signup) EditText passwordInput;
    @BindView(R.id.name_signup) EditText nameInput;
    @BindView(R.id.phone_signup) EditText phoneInput;
    @BindView(R.id.create_account) Button createAccButton;
    @BindView(R.id.profileButton) ImageButton profileImgButton;
    @BindView(R.id.nice_spinner) NiceSpinner currencyList;
    @BindView(R.id.profile_image) ImageView displayImg;

    public String email;
    public String password;
    public String name;
    public String phone;
    public String currencyCode;
    public String image;

    public ProgressDialog progressDialog;

    public static final int GET_FROM_GALLERY = 1;
    public static final int CAPTURE_FROM_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        currencyList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        currencyCode = code;
                        // NiceSpinner attach data source
                        currencyList.setText(currencyCode);
                    }

                });
                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
                //picker.dismiss();
            }

        });

        progressDialog = new ProgressDialog(this, R.style.AppTheme);

        profileImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();
            }
        });

        createAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    public void createAccount(){
        if (!validate()) {
            onSignupFailed();
            return;
        }

        createAccButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                //, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Creating an account...");
        progressDialog.show();

        this.email = emailInput.getText().toString();
        this.password = passwordInput.getText().toString();
        this.name = nameInput.getText().toString();
        this.phone = phoneInput.getText().toString();

        // Call thread to access DAO and check whether the email is taken or not
        new insertAsyncTask(this.email, this.password, this.name, this.phone, this.currencyCode, this.image).execute();
//        new insertAsyncTask(this.email, this.password, this.name, this.phone).execute();

    }

    public void uploadImg() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera")){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = getFile();
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(cameraIntent, CAPTURE_FROM_CAMERA);
                }
                else if(items[which].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), GET_FROM_GALLERY);
                }
                else if(items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);

//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureDirectoryPath = pictureDirectory.getPath();
//
//        Uri data = Uri.parse(pictureDirectoryPath);
//
//        photoPickerIntent.setDataAndType(data, "image/*");
//
//        startActivityForResult(photoPickerIntent, GET_FROM_GALLERY);

//        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GET_FROM_GALLERY);


    }

    private class insertAsyncTask extends AsyncTask<String, Void, Boolean> {

        private boolean exist;
        private String email;
        private String password;
        private String name;
        private String phone;
        private String currency;
        private String image;

        insertAsyncTask(String email, String password, String name, String phone, String currency, String image) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.phone = phone;
            this.currency = currency;
            this.image = image;
        }

//        insertAsyncTask(String email, String password, String name, String phone) {
//            this.email = email;
//            this.password = password;
//            this.name = name;
//            this.phone = phone;
//        }

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
                createAccButton.setEnabled(true);
            }
        }
    }

    public boolean validate() {
        // Check only email, password, name, phone, currency
        boolean valid = true;

        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        name = nameInput.getText().toString();
        phone = phoneInput.getText().toString();

        // Check email address by regex ...@....
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

        if (phone.isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()) {
            phoneInput.setError("input only number");
            valid = false;
        } else {
            phoneInput.setError(null);
        }

        // Check currency list spinner
        if ( currencyCode.isEmpty()) {
            currencyList.setError("select default currency");
            valid = false;
        } else {
            currencyList.setError(null);
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
                User user = new User(email, password, name, phone, currencyCode, image);
//                User user = new User(email, password, name, phone);
                AomNgernDatabase.getDatabase(getApplicationContext()).userDao().addUser(user);
            }
        }).start();

        createAccButton.setEnabled(true);

        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            this.image = selectedImage.toString();
            displayImg.setImageURI(selectedImage);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if(requestCode == CAPTURE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Uri selectedImage = data.getData();
            this.image = selectedImage.toString();
            final Bitmap bmp = (Bitmap) bundle.get("data");
            displayImg.setImageBitmap(bmp);

        }
    }

}
