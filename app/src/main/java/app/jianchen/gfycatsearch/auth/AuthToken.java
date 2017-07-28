package app.jianchen.gfycatsearch.auth;

/**
 * Created by jianchen on 7/27/17.
 */

//{ "token_type": "bearer", "scope": "", "expires_in": 3600, "access_token": "your_token"}
public class AuthToken {

    private final double expires_in;
    private final String access_token;
    private double createdAt = System.currentTimeMillis();

    public AuthToken(String token, double expiry) {
        this.access_token = token;
        this.expires_in = expiry;
    }

    public boolean isExpired() {
        if (expires_in + createdAt > System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    public double getExpiry() {
        return expires_in;
    }
    public String getToken() {
        return access_token;
    }
}
