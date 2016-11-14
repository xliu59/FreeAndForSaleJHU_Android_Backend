package com.team16.project.sqlite;

import com.team16.project.User.User;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;



/**
 * This class is used to interact with database.
 * @author OOSE_Team16
 */
public class MyDB {
    private String dbNmae;

    /**
     * This method will first check the existence of target database.
     * If database does not exist, create one.
     */
    public void initializeDB() {
        Connection conn = null;
        Statement sta = null;
        try {
            String targetDB = "/Users/HangBao/Documents/2016fall/OOSE/project/iteration4/help_sesion/version2/Backend/ProjectDB.db";

            if (!(new File(targetDB).isFile())) {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbNmae);
                conn.setAutoCommit(false);
                System.out.println("Database doesn't exist");


            } else {
                System.out.println("Database already exists!");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Method to search for user's email and password in database.
     * If matched, return true. Otherwise, return false.
     * @param email : provided email.
     * @param password : provided password
     * @return true if email and password match with data in DB. Otherwise, return false.
     */
    public Boolean searchUserLogin(String email, String password){
        Connection conn = null;
        Statement sta = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbNmae);
            conn.setAutoCommit(false);
            System.out.println("Connect database successfully");

            sta = conn.createStatement();
            String sql = "SELECT * FROM UserLogin " +
                    "WHERE email = '" + email + "' AND password = '" + password +"';";

            ResultSet results = sta.executeQuery(sql);
            if (!results.isBeforeFirst()) {
                System.out.println("Empty Sets");
                results.close();
                sta.close();
                conn.close();
                return false;
            } else {
                System.out.println("Find User");
                results.close();
                sta.close();
                conn.close();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        return false;
    }

    /**
     * Method to show user's detail info. in the database.
     * @param userId : userId.
     * @return If the user exists, return a hashMap containing required information, otherwise return an empty hashMap.
     */
    public HashMap<String, Object> searchUserDetailInfo(String userId){
        Connection conn = null;
        Statement sta = null;
        HashMap<String, Object> userDetail = new HashMap<String, Object>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbNmae);
            conn.setAutoCommit(false);
            System.out.println("Connect database successfully");
            sta = conn.createStatement();
            String sql = "SELECT username, email, address, phone, zipCode "
                    + "FROM UserDetail INNER JOIN ContactInfo "
                    + "ON UserDetail.contactId = ContactInfo.contactId"
                    + "WHERE userId = '" + userId + "';";

            ResultSet results = sta.executeQuery(sql);

            if (!results.isBeforeFirst()) {
                System.out.println("The user dose not exist!");
            } else {
                userDetail.put("userId", userId);
                userDetail.put("username", results.getString("username"));
                userDetail.put("email", results.getString("email"));
                userDetail.put("phone", results.getString("phone"));
                userDetail.put("address", results.getString("address"));
                userDetail.put("zipCode", results.getString("zipCode"));

            	/*
            	userDetail.put("city", results.getString("city"));
            	userDetail.put("state", results.getString("state"));
            	userDetail.put("imgLink", results.getString("imgLink"));
            	userDetail.put("facebook", results.getString("facebook"));
            	*/
                System.out.println("Find UserDetail");
            }
            results.close();
            sta.close();
            conn.close();
            return userDetail;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        return userDetail;
    }

    /**
     * Method to update user's detail info. in the database.
     * If the contact info. of the user exists, we update the information in database
     * If the user didn't fill the detail, we insert a new tuple to the database
     * @param sessionId : userId.
     * @return return true if success, otherwise false.
     */

    public Boolean updateAccountInfo(String sessionId, User user) {
        Connection conn = null;
        PreparedStatement sta_udetail = null;
        PreparedStatement sta_contact = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbNmae);
            conn.setAutoCommit(false);
            System.out.println("Connect database successfully");

            // update userDetail table
            String sql_uDetail = "UPDATE UserDetail SET username = ? WHERE userId = ?";
            sta_udetail = conn.prepareStatement(sql_uDetail);
            sta_udetail.setString(1, user.getUsername());
            sta_udetail.setString(2, sessionId);
            int rowsUpdated_uDetail = sta_udetail.executeUpdate();


            // update ContactInfo table
            String sql_contact = "UPDATE ContactInfo SET email = ?, phone = ?, address = ?, zipCode = ? "
                    + "WHERE contactId = (SELECT contactId FROM UserDetail WHERE userId = ?)";

            sta_contact = conn.prepareStatement(sql_contact);
            sta_contact.setString(1, user.getEmail());
            sta_contact.setString(2, user.getPhoneNum());
            sta_contact.setString(3, user.getAddress());
            sta_contact.setString(4, user.getZipCode());
            sta_contact.setString(5, sessionId);
            int rowsUpdated_contact = sta_contact.executeUpdate();

            if (rowsUpdated_uDetail > 0 && rowsUpdated_contact > 0) {
                System.out.println("An existing user info. was updated successfully!");
                return true;
            } else {
                System.out.println("Update Fail");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        return false;
    }


    public MyDB(String dbNmae) {
        this.dbNmae = dbNmae;
    }

    public String getDbNmae() {
        return this.dbNmae;
    }
}

