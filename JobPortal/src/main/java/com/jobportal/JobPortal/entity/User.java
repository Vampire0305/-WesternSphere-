package com.jobportal.JobPortal.entity;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Enum getRole() {
        return Role;
    }

    public void setRole(Enum role) {
        Role = role;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String name;
    @Column(unique=true)
    private String email;
    private String password;
    private Enum Role;
}
