package com.jobportal.JobPortal.entity;

import com.jobportal.JobPortal.Enum.Role;

import javax.persistence.Entity;


import javax.persistence.*;



@Entity
@Table(name="users")

public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long Id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {}

    public User(String name,String email,String password,Role role) {
        this.name=name;
        this.email=email;
        this.password=password;
        this.role=role;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}