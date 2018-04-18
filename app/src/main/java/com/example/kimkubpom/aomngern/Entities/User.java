// Kornkamol Chirasupakul 5888024
// Jidapa Kongsakoonwong 5888081
// Phanaphon Sereelertwiwat 5888157

// User entity or the table in database that keeps all of the users information

package com.example.kimkubpom.aomngern.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by kimkubpom on 18/4/2018 AD.
 */

@Entity(tableName = "User")
public class User {

    @Ignore
    public User(String UserEmail, String UserPassword, String UserName, String UserPhone) {
        UserID = UUID.randomUUID().toString();
        this.UserName = UserName;
        this.UserPassword = UserPassword;
        this.UserEmail = UserEmail;
        this.UserPhone = UserPhone;
//        this.UserPhoto = Photo;
//        this.UserCurrency = Currency;
    }

    //    @Ignore
    public User(String UserID, String UserEmail, String UserPassword, String UserName, String UserPhone) {
        this.UserID = UserID;
        this.UserName = UserName;
        this.UserPassword = UserPassword;
        this.UserEmail = UserEmail;
        this.UserPhone = UserPhone;
//        this.UserPhoto = Photo;
//        this.UserCurrency = Currency;
    }

    @NonNull
    public String getUserID() {
        return UserID;
    }

    public void setUserID(@NonNull String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

//    public String getUserCurrency() {
//        return UserCurrency;
//    }
//
//    public void setUserCurrency(String userCurrency) {
//        UserCurrency = userCurrency;
//    }
//
//    public String getUserPhoto() {
//        return UserPhoto;
//    }
//
//    public void setUserPhoto(String userPhoto) {
//        UserPhoto = userPhoto;
//    }

    @NonNull


    @PrimaryKey
    @ColumnInfo(name = "UserID")
    private String UserID;

    @ColumnInfo(name = "UserEmail")
    private String UserEmail;

    @ColumnInfo(name = "UserPassword")
    private String UserPassword;

    @ColumnInfo(name = "UserName")
    private String UserName;

    @ColumnInfo(name = "UserPhone")
    private String UserPhone;

//    @ColumnInfo(name = "UserCurrency")
//    private String UserCurrency;
//
//    @ColumnInfo(name = "UserPhoto")
//    private String UserPhoto;



}