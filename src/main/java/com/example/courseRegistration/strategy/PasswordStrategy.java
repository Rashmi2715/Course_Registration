// strategy/PasswordStrategy.java
package com.example.courseRegistration.strategy;

public interface PasswordStrategy {
    boolean match(String rawPassword, String storedPassword);
}
