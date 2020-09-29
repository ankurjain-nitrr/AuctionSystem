package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private String id;
    @Setter
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
