package model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private String id;
    private String name;
    private String email;
    private String passwordMD5;
    private long created;

    public User(String name, String email, String passwordMD5) {
        this.id = UUID.randomUUID().toString();
        this.created = System.currentTimeMillis();
        this.name = name;
        this.email = email;
        this.passwordMD5 = passwordMD5;
    }

}
