package springboot.infrastructure.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springboot.domain.security.model.Role;
import springboot.domain.security.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        String testSecret = "SuperSecretKeyForJWTAuthSystemWhichMustBeAtLeast256BitsLong1234567890";
        long expiration = 900000L; // 15 mins
        jwtTokenService = new JwtTokenService(testSecret, expiration);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        Role role = Role.create("USER");
        User user = User.create("test@mindconnect.com", "hash", role);

        String token = jwtTokenService.generateAccessToken(user);
        assertNotNull(token);
        assertFalse(token.isBlank());

        String userId = jwtTokenService.validateAccessToken(token);
        assertEquals(user.getId(), userId);

        String email = jwtTokenService.extractEmail(token);
        assertEquals("test@mindconnect.com", email);

        List<String> roles = jwtTokenService.extractRoles(token);
        assertEquals(1, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
    }

    @Test
    void shouldGenerateRefreshToken() {
        String refreshToken = jwtTokenService.generateRefreshToken();
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isBlank());
    }
}
