package com.example.mealwise.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static final int MIN_PASSWORD_LENGTH = 6;

    // Regex للإيميل
    public static final String EMAIL_PATTERN =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+";


    public static String getEmailError(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email cannot be empty";
        }
        if (!Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
            return "Invalid email address format";
        }
        return null;
    }

    public static String getPasswordError(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }

        if (!password.matches(".*[@#$%^&+=!_].*")) {
            return "Password must contain at least one special character (@#$%^&+=!_)";
        }

        return null;
    }

    public static String getConfirmPasswordError(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return "Please confirm your password";
        }
        if (!confirmPassword.equals(password)) {
            return "Passwords do not match";
        }
        return null;
    }

    public static String getUsernameError(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty";
        }
        return null;
    }
}