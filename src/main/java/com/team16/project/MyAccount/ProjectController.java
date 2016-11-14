package com.team16.project.MyAccount;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.util.List;


/**
 * Code for controller.
 * This class is used as controller to receive request and send response.
 * @author OOSE_Team16
 */
public class ProjectController {

    private static final String API_CONTEXT = "/login_test";

    public final ProjectService projectService;

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        /**
         * Handling login request.
         */

        get(API_CONTEXT, "application/json", (request, response) -> {
            response.status(200);
            String email = request.queryParams("email");
            String password = request.queryParams("password");

            HashMap<String, String> result = new HashMap<String, String>();

            result.put("result", login(email, password).toString());
            return result;
        }, new JsonTransformer());

        /**
         * show a user's detail information
         */
        get(API_CONTEXT + "/MyAccount/:sessionId", "application/json", (request, response) -> {
            try {
                return projectService.showUserDetailInfo(request.params(":sessionId"));
            } catch (ProjectService.projectServiceException ex) {
                logger.error(String.format("Incorrect userId: %s", request.params(":sessionId")));
                response.status(404);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        /**
         * update an user's account detail Info (for the first time login)
         */
        post(API_CONTEXT + "/MyAccount/AccountModify/:sessionId", "application/json", (request, response) -> {
            try {
                System.out.println(request.body());
                return projectService.updateAccountInfo(request.params(":sessionId"), request.body());
            } catch (ProjectService.projectServiceException ex) {
                logger.error(String.format("Failed to update accountInfo with sessionId: %s", request.params(":sessionId")));
                response.status(405);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());


        /**
         * Handling register request.
         */
        post("/register/email", (request, response) -> register(request, response));

    }

    public Boolean login(String email, String password){

        return projectService.checkLogin(email, password);
    }

    public int register(Request request, Response response){
        Connection c = null;
        PreparedStatement stmt = null;
        String sql = "SELECT Email " + "FROM UserLogin " + "WHERE Email = ?";

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(request.body());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String email =  (String) jsonObject.get("email");

        try {
            c = DriverManager.getConnection("jdbc:sqlite:ProjectDB.db");
            stmt = c.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            String result = rs.getString("email");
            rs.close();
            stmt.close();
            c.close();
            System.out.println("Email already exists.");
            return 0;
        }
        catch ( Exception e ) {
            System.out.println("Email has not been used.");
            return 1;
        }
    }
}

