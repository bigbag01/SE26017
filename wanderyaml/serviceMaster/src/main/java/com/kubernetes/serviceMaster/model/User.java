package com.kubernetes.serviceMaster.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by bambihui on 2019/6/28.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}


