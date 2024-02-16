package com.example.userlogin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role rule;

    public User() {
    }

    
    


    public User(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public enum Role {
        ADMIN, USER
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
   
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Role getRule() {
        return rule;
    }
    public void setRule(Role rule) {
        this.rule = rule;
    }
    // toString method for logging and debugging purposes
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                // Never print passwords in logs
                ", password='[PROTECTED]'" +
                '}';
    }
}
