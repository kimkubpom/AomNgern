// Kornkamol Chirasupakul 5888024
// Jidapa Kongsakoonwong 5888081
// Phanaphon Sereelertwiwat 5888157

// User DAO or the interface in order to retrieve the data from User entities and in database
// This contains all of the query written in java language

package com.example.kimkubpom.aomngern.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.kimkubpom.aomngern.Entities.User;

import java.util.List;

/**
 * Created by kimkubpom on 18/4/2018 AD.
 */

@Dao
public interface UserDao {

//    /**
//     * Get the user from the table. Since for simplicity we only have one user in the database,
//     * this query gets all users from the table, but limits the result to just the 1st user.
//     *
//     * @return the user from the table
//     */
////    @Query("SELECT * FROM User LIMIT 1")
////    Flowable<User> getUser();
//
//    /**
//     * Insert a user in the database. If the user already exists, replace it.
//     *
//     * @param user the user to be inserted.
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertUser(User user);
//
//    /**
//     * Delete all users.
//     */
//    @Query("DELETE FROM User")
//    void deleteAllUsers();

    @Query("SELECT * FROM User")
    List<User> getUser();

    @Query("SELECT * FROM User where UserID = :id")
    User getUserbyId(String id);

    @Query("SELECT * FROM User where UserEmail = :email")
    User getUserbyEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Query("DELETE FROM User")
    void deleteAll();

}