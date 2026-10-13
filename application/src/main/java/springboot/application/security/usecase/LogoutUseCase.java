package springboot.application.security.usecase;

import springboot.application.security.dto.RefreshTokenRequest;
import springboot.domain.security.exception.InvalidTokenException;
import springboot.domain.security.model.RefreshToken;
import springboot.domain.security.port.repository.RefreshTokenRepository;

public class LogoutUseCase {
    private final RefreshTokenRepository refreshTokenRepository;

    public LogoutUseCase(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void execute(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new InvalidTokenException("Token de actualización no encontrado"));

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);
    }
}
