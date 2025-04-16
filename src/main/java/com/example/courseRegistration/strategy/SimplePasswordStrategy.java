
package com.example.courseRegistration.strategy;

public class SimplePasswordStrategy implements PasswordStrategy {
    @Override
    public boolean match(String rawPassword, String storedPassword) {
        return rawPassword.equals(storedPassword);
    }
}
