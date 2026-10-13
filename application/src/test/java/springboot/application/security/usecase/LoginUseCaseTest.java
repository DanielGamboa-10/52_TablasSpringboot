package springboot.application.security.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springboot.application.security.dto.LoginRequest;
import springboot.application.security.dto.LoginResponse;
import springboot.domain.security.exception.InvalidCredentialsException;
import springboot.domain.security.exception.UserBlockedException;
import springboot.domain.security.model.RefreshToken;
import springboot.domain.security.model.Role;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.RefreshTokenRepository;
import springboot.domain.security.port.repository.UserRepository;
import springboot.domain.security.port.service.PasswordService;
import springboot.domain.security.port.service.TokenService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LoginUseCaseTest {

    private InMemoryUserRepository userRepository;
    private InMemoryRefreshTokenRepository refreshTokenRepository;
    private FakePasswordService passwordService;
    private FakeTokenService tokenService;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        refreshTokenRepository = new InMemoryRefreshTokenRepository();
        passwordService = new FakePasswordService();
        tokenService = new FakeTokenService();
        loginUseCase = new LoginUseCase(
                userRepository,
                passwordService,
                tokenService,
                refreshTokenRepository,
                604800000L,
                900L
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        Role role = Role.create("USER");
        User user = User.create("test@mindconnect.com", "encoded-pass", role);
        userRepository.save(user);

        LoginRequest request = new LoginRequest("test@mindconnect.com", "raw-pass");
        LoginResponse response = loginUseCase.execute(request);

        assertNotNull(response);
        assertEquals("dummy-jwt-token", response.accessToken());
        assertNotNull(response.refreshToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(900L, response.expiresIn());
    }

    @Test
    void shouldRejectInvalidPassword() {
        Role role = Role.create("USER");
        User user = User.create("test@mindconnect.com", "encoded-pass", role);
        userRepository.save(user);

        LoginRequest request = new LoginRequest("test@mindconnect.com", "wrong-password");
        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.execute(request));
    }

    @Test
    void shouldRejectBlockedUser() {
        Role role = Role.create("USER");
        User user = User.create("test@mindconnect.com", "encoded-pass", role);
        user.block();
        userRepository.save(user);

        LoginRequest request = new LoginRequest("test@mindconnect.com", "raw-pass");
        assertThrows(UserBlockedException.class, () -> loginUseCase.execute(request));
    }

    @Test
    void shouldRejectNonExistentUser() {
        LoginRequest request = new LoginRequest("unknown@mindconnect.com", "any");
        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.execute(request));
    }

    private static class InMemoryUserRepository implements UserRepository {
        private final Map<String, User> byId = new HashMap<>();
        private final Map<String, User> byEmail = new HashMap<>();

        @Override
        public User save(User user) {
            byId.put(user.getId(), user);
            byEmail.put(user.getEmail(), user);
            return user;
        }

        @Override
        public Optional<User> findById(String id) {
            return Optional.ofNullable(byId.get(id));
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.ofNullable(byEmail.get(email));
        }

        @Override
        public boolean existsByEmail(String email) {
            return byEmail.containsKey(email);
        }

        @Override
        public void deleteById(String id) {
            User removed = byId.remove(id);
            if (removed != null) byEmail.remove(removed.getEmail());
        }
    }

    private static class InMemoryRefreshTokenRepository implements RefreshTokenRepository {
        private final Map<String, RefreshToken> byToken = new HashMap<>();

        @Override
        public RefreshToken save(RefreshToken refreshToken) {
            byToken.put(refreshToken.getToken(), refreshToken);
            return refreshToken;
        }

        @Override
        public Optional<RefreshToken> findByToken(String token) {
            return Optional.ofNullable(byToken.get(token));
        }

        @Override
        public Optional<RefreshToken> findByUserId(String userId) {
            return byToken.values().stream().filter(r -> r.getUserId().equals(userId)).findFirst();
        }

        @Override
        public void revokeByUserId(String userId) {
            byToken.values().stream().filter(r -> r.getUserId().equals(userId)).forEach(RefreshToken::revoke);
        }

        @Override
        public void deleteByUserId(String userId) {
            byToken.entrySet().removeIf(e -> e.getValue().getUserId().equals(userId));
        }
    }

    private static class FakePasswordService implements PasswordService {
        @Override
        public String hash(String rawPassword) {
            return "encoded-" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String encodedPassword) {
            return "raw-pass".equals(rawPassword) && "encoded-pass".equals(encodedPassword);
        }
    }

    private static class FakeTokenService implements TokenService {
        @Override
        public String generateAccessToken(User user) {
            return "dummy-jwt-token";
        }

        @Override
        public String generateRefreshToken() {
            return UUID.randomUUID().toString();
        }

        @Override
        public String validateAccessToken(String token) {
            return "dummy-user-id";
        }

        @Override
        public List<String> extractRoles(String token) {
            return List.of("ROLE_USER");
        }

        @Override
        public String extractEmail(String token) {
            return "test@mindconnect.com";
        }
    }
}
