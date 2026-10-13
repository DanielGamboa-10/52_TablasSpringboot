package springboot.infrastructure.security.adapters.out.persistence.adapter;

import org.springframework.stereotype.Component;
import springboot.domain.security.model.RefreshToken;
import springboot.domain.security.port.repository.RefreshTokenRepository;
import springboot.infrastructure.security.adapters.out.persistence.entity.RefreshTokenEntity;
import springboot.infrastructure.security.adapters.out.persistence.mappers.SecurityPersistenceMapper;
import springboot.infrastructure.security.adapters.out.persistence.repository.RefreshTokenJpaRepository;

import java.util.Optional;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;
    private final SecurityPersistenceMapper mapper;

    public RefreshTokenRepositoryAdapter(RefreshTokenJpaRepository jpaRepository, SecurityPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = mapper.toEntityRefreshToken(refreshToken);
        RefreshTokenEntity saved = jpaRepository.save(entity);
        return mapper.toDomainRefreshToken(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toDomainRefreshToken);
    }

    @Override
    public Optional<RefreshToken> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).map(mapper::toDomainRefreshToken);
    }

    @Override
    public void revokeByUserId(String userId) {
        jpaRepository.revokeByUserId(userId);
    }

    @Override
    public void deleteByUserId(String userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
