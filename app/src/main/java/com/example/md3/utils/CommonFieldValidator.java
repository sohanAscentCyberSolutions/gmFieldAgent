package com.example.md3.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonFieldValidator {

    public static boolean isValidIfsc(String IFSC) {

        Pattern pattern;
        Matcher matcher;

        final String IFSC_PATTERN = "^[A-Za-z]{4}[a-zA-Z0-9]{7}$";

        pattern = Pattern.compile(IFSC_PATTERN);
        matcher = pattern.matcher(IFSC);

        return matcher.matches();

    }

    public static boolean isValidMobile(String mobileNumber) {
        boolean isValid = false;
        if (mobileNumber.equals("")) {
            isValid= true;
        } else if (mobileNumber.length() >= 8) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isEmailValid(String email) {
        if (email.equals("")) {
            return false;
        }
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isAadharValid(String aadhar) {
        String expression = "^\\d{4}\\s\\d{4}\\s\\d{4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(aadhar);
        return matcher.matches();
    }

    public static boolean isNameValid(String name) {
        if (name.equals("")) {
            return true;
        }
        String expression = "[a-zA-Z ]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }


    public static boolean isValidDouble(String amount) {
        if (amount.equals("")) {
            return true;
        }
        String expression = "^(\\d*\\.)?\\d+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(amount);
        return matcher.matches();
    }
}
