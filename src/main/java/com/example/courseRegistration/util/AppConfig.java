package com.example.courseRegistration.util;

public class AppConfig {
    // 1. Create a private static instance of the class
    private static AppConfig instance;

    // 2. Private constructor to prevent instantiation
    private AppConfig() {
        // Initialization logic here
    }

    // 3. Public method to return the single instance
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    // 4. Sample method
    public String getAppName() {
        return "Course Registration System";
    }
}
