package com.team16.project.MyAccount;

import com.google.gson.Gson;
import com.team16.project.User.User;
import com.team16.project.sqlite.MyDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import javax.sql.DataSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;



/**
 * Code for model.
 * This class is used as model to do underlying logic.
 * @author OOSE_Team16
 */
public class ProjectService {


    private final MyDB projectDB;
    // private Sql2o projectDB;

    private final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public ProjectService(MyDB projectDB) {
        this.projectDB = projectDB;
    }

    /**
     * @param email email received from request.
     * @param password password received from request.
     * @return boolean value, if login successfully, return true, else return false.
     */
    public Boolean checkLogin(String email, String password) {
        return projectDB.searchUserLogin(email, password);
    }

    /**
     * Find a user's detail info. corresponding to a given sessionId
     * @param userId
     * @return If the user exists, return a hashMap containing required information, otherwise return an empty hashMap.
     *
     */
    public HashMap<String, Object> showUserDetailInfo(String userId) throws projectServiceException{
        return projectDB.searchUserDetailInfo(userId);
    }

    /**
     * Update the specified account entry with new information
     * the email and phone must not be missing
     * re-confirmation is needed if the user change email address or phone number
     * @param sessionId
     * @param body
     * @return the updated User detail
     *
     */
    public HashMap<String, Object> updateAccountInfo(String sessionId, String body) throws projectServiceException {

        User user = new Gson().fromJson(body, User.class);
    	/* email and phone must not missing! (check in the front end)
    	   if email or phone is updated, we need to send verification. (do here)
    	   how to throw exception?
    	 */
        Boolean isUpdate = projectDB.updateAccountInfo(sessionId, user);
        if (isUpdate) {
            return showUserDetailInfo(sessionId);
        } else {
            return null;
        }
    }

    public class projectServiceException extends Exception {
        public projectServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}



