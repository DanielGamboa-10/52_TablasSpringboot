package springboot.domain.security.port.repository;

import springboot.domain.security.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(String userId);
    void revokeByUserId(String userId);
    void deleteByUserId(String userId);
}
