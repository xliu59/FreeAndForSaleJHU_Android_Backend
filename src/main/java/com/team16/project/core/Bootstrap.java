package com.team16.project.core;


import com.team16.project.registration.email.EmailRegistrationController;
import com.team16.project.registration.password.PasswordController;
import com.team16.project.registration.phone.PhoneRegistrationController;


import java.sql.SQLException;

public class Bootstrap {
    public static final String DATABASE = "jdbc:sqlite:ProjectDB.db";

    public static void main(String[] args) throws SQLException {
        new EmailRegistrationController();
        new PhoneRegistrationController();
        new PasswordController();
    }
}
