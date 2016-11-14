package com.team16.project.registration.email;

import java.sql.*;

import static spark.Spark.*;

public class EmailRegistrationController {
    private static final String EMAIL_VERIFICATION_API = "/registration/email";
    private EmailRegistrationService emailRegistrationService;

    public EmailRegistrationController() throws SQLException {
        emailRegistrationService = new EmailRegistrationService();
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(EMAIL_VERIFICATION_API, (req, res) ->
        {
           return emailRegistrationService.verifyEmail(req.body());
        });
    }
}
