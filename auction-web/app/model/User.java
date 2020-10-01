package model;

import lombok.*;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private String id;
    @Setter
    private String name;
    private String email;
    private String passwd;
    private long created;

    private User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String name, String email, String passwd) {
        this.id = UUID.randomUUID().toString();
        this.created = System.currentTimeMillis();
        this.name = name;
        this.email = email;
        this.passwd = passwd;
    }

}
