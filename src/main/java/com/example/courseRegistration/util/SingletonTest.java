package com.example.courseRegistration.util;

class SingletonTest {
    public static void main(String[] args) {
        AppConfig config1 = AppConfig.getInstance();
        AppConfig config2 = AppConfig.getInstance();

        if (config1 == config2) {
            System.out.println("Singleton works! Both are the same instance.");
        } else {
            System.out.println("Singleton failed! Different instances.");
        }

        System.out.println("App Name: " + config1.getAppName());
    }
}
