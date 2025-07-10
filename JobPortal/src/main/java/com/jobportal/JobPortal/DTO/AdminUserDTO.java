package com.jobportal.JobPortal.DTO;

import com.jobportal.JobPortal.Enum.Role;

public class AdminUserDTO {

    public Long id;
    public String name;
    public String email;
    public Role role;
    public boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean blocked;

    public AdminUserDTO() {}
    public AdminUserDTO(Long id,String name,String email,Role role,boolean active,boolean blocked) {

        this.id=id;
        this.name=name;
        this.email=email;
        this.role=role;
        this.active=active;
        this.blocked=blocked;
    }

}
