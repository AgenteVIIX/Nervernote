package com.example.nevernote;

import java.io.Serializable;

public class User implements Serializable {
    private String username, password, email;
    private int id;

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
