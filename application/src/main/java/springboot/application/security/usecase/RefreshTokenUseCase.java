package springboot.application.security.usecase;

import springboot.application.security.dto.LoginResponse;
import springboot.application.security.dto.RefreshTokenRequest;
import springboot.domain.security.exception.InvalidTokenException;
import springboot.domain.security.exception.UserBlockedException;
import springboot.domain.security.exception.UserNotFoundException;
import springboot.domain.security.model.RefreshToken;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.RefreshTokenRepository;
import springboot.domain.security.port.repository.UserRepository;
import springboot.domain.security.port.service.TokenService;

public class RefreshTokenUseCase {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final long accessTokenExpirationSeconds;

    public RefreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            TokenService tokenService,
            long accessTokenExpirationSeconds) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    public LoginResponse execute(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new InvalidTokenException("Token de actualización no encontrado"));

        if (!refreshToken.isValid()) {
            throw new InvalidTokenException("Token de actualización revocado o expirado");
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuario asociado no encontrado"));

        if (!user.isActive()) {
            throw new UserBlockedException("El usuario está bloqueado o inactivo");
        }

        String newAccessToken = tokenService.generateAccessToken(user);
        return LoginResponse.of(newAccessToken, refreshToken.getToken(), accessTokenExpirationSeconds);
    }
}
