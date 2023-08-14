package com.dev.rolebasedauthorization.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private boolean active;
    private String roles;
}
