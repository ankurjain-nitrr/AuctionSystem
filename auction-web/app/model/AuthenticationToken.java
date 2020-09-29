package model;

import ch.qos.logback.classic.db.names.ColumnName;
import lombok.Getter;
import utils.Constants;

@Getter
public class AuthenticationToken {

    private String userID;
    private String token;
    private long created;
    private long expiry;

    public AuthenticationToken(String userID, String token, int expiryAfterDays) {
        this.userID = userID;
        this.token = token;
        long currentTime = System.currentTimeMillis();
        this.created = currentTime;
        this.expiry = currentTime + (expiryAfterDays * Constants.DAY_IN_MS);
    }
}
