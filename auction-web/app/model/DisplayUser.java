package model;

import lombok.Getter;

@Getter
public class DisplayUser {

    private String userId;
    private String name;

    public DisplayUser(User user) {
        this.userId = user.getId();
        this.name = user.getName();
    }

}
