package springboot.application.security.usecase;

import springboot.application.security.dto.LoginRequest;
import springboot.application.security.dto.LoginResponse;
import springboot.domain.security.exception.InvalidCredentialsException;
import springboot.domain.security.exception.UserBlockedException;
import springboot.domain.security.model.RefreshToken;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.RefreshTokenRepository;
import springboot.domain.security.port.repository.UserRepository;
import springboot.domain.security.port.service.PasswordService;
import springboot.domain.security.port.service.TokenService;

public class LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpirationMillis;
    private final long accessTokenExpirationSeconds;

    public LoginUseCase(
            UserRepository userRepository,
            PasswordService passwordService,
            TokenService tokenService,
            RefreshTokenRepository refreshTokenRepository,
            long refreshTokenExpirationMillis,
            long accessTokenExpirationSeconds) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    public LoginResponse execute(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!user.isActive()) {
            throw new UserBlockedException("El usuario está bloqueado o inactivo");
        }

        if (!passwordService.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = RefreshToken.create(user.getId(), refreshTokenExpirationMillis);
        refreshTokenRepository.save(refreshToken);

        return LoginResponse.of(accessToken, refreshToken.getToken(), accessTokenExpirationSeconds);
    }
}
