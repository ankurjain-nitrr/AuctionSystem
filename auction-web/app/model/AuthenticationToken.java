package model;

import ch.qos.logback.classic.db.names.ColumnName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import utils.Constants;

@EqualsAndHashCode
@Getter
public class AuthenticationToken {

    private String userId;
    private String token;
    private long created;
    private long expiry;

    public AuthenticationToken(String userId, String token, int expiryAfterDays) {
        this.userId = userId;
        this.token = token;
        long currentTime = System.currentTimeMillis();
        this.created = currentTime;
        this.expiry = currentTime + (expiryAfterDays * Constants.DAY_IN_MS);
    }

    public AuthenticationToken(String userId, String token, long created, long expiry) {
        this.userId = userId;
        this.token = token;
        this.created = created;
        this.expiry = expiry;
    }

    public boolean validate(String userId) {
        return expiry > System.currentTimeMillis() && belongsTo(userId);
    }

    private boolean belongsTo(String userId) {
        return this.userId.equals(userId);
    }

}
