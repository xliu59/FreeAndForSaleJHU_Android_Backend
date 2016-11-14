package com.team16.project.core;

import com.team16.project.MyAccount.ProjectController;
import com.team16.project.MyAccount.ProjectService;
import com.team16.project.registration.email.EmailRegistrationController;
import com.team16.project.registration.password.PasswordController;
import com.team16.project.registration.phone.PhoneRegistrationController;
import com.team16.project.sqlite.MyDB;

import java.sql.SQLException;

import static spark.Spark.ipAddress;
import static spark.Spark.port;

public class Bootstrap {
    public static final String DATABASE = "jdbc:sqlite:ProjectDB.db";

    public static void main(String[] args) throws SQLException {
        new EmailRegistrationController();
        new PhoneRegistrationController();
        new PasswordController();

        // Todo: refactor
        MyDB projectDB = new MyDB("ProjectDB.db");
        projectDB.initializeDB();

        ProjectService model = new ProjectService(projectDB);
        new ProjectController(model);


    }
}
