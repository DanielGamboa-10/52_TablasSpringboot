package springboot.domain.security.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class RefreshToken {
    private final String id;
    private final String userId;
    private final String token;
    private final Instant expiresAt;
    private boolean revoked;

    public RefreshToken(String id, String userId, String token, Instant expiresAt, boolean revoked) {
        this.id = Objects.requireNonNullElseGet(id, () -> UUID.randomUUID().toString());
        this.userId = Objects.requireNonNull(userId, "userId no puede ser nulo");
        this.token = Objects.requireNonNull(token, "token no puede ser nulo");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt no puede ser nulo");
        this.revoked = revoked;
    }

    public static RefreshToken create(String userId, long expirationMillis) {
        String tokenValue = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusMillis(expirationMillis);
        return new RefreshToken(UUID.randomUUID().toString(), userId, tokenValue, expiry, false);
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !this.revoked && !isExpired();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getToken() { return token; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isRevoked() { return revoked; }
}
