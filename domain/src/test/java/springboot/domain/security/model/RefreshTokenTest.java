package springboot.domain.security.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    @Test
    void shouldCreateValidRefreshToken() {
        RefreshToken token = RefreshToken.create("user-123", 60000);

        assertNotNull(token.getId());
        assertEquals("user-123", token.getUserId());
        assertNotNull(token.getToken());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());
        assertTrue(token.isValid());
    }

    @Test
    void shouldRevokeToken() {
        RefreshToken token = RefreshToken.create("user-123", 60000);
        assertTrue(token.isValid());

        token.revoke();
        assertTrue(token.isRevoked());
        assertFalse(token.isValid());
    }

    @Test
    void shouldDetectExpiredToken() {
        RefreshToken expired = new RefreshToken(
                "token-id",
                "user-123",
                "uuid-token",
                Instant.now().minusSeconds(10),
                false
        );

        assertTrue(expired.isExpired());
        assertFalse(expired.isValid());
    }
}
