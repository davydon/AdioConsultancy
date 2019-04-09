package com.management.model.Users;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.management.model.AbstractModel;
import com.management.utility.CustomDateSerializer;

import java.io.Serializable;
import java.security.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreviousPasswords extends AbstractModel implements Serializable {


    private Long id   ;
    private String password ;
    private Long userId    ;
    //ON DELETE CASCADE,

    @JsonSerialize(using = CustomDateSerializer.class)
    private Timestamp created_on;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Timestamp created_on) {
        this.created_on = created_on;
    }
}
