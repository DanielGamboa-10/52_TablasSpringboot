package springboot.infrastructure.security.adapters.out.persistence.mappers;

import org.springframework.stereotype.Component;
import springboot.domain.security.model.RefreshToken;
import springboot.domain.security.model.Role;
import springboot.domain.security.model.User;
import springboot.domain.security.model.UserStatus;
import springboot.infrastructure.security.adapters.out.persistence.entity.RefreshTokenEntity;
import springboot.infrastructure.security.adapters.out.persistence.entity.RoleEntity;
import springboot.infrastructure.security.adapters.out.persistence.entity.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityPersistenceMapper {

    public Role toDomainRole(RoleEntity entity) {
        if (entity == null) return null;
        return new Role(entity.getId(), entity.getName(), entity.getAuthority());
    }

    public RoleEntity toEntityRole(Role domain) {
        if (domain == null) return null;
        return new RoleEntity(domain.getId(), domain.getName(), domain.getAuthority());
    }

    public User toDomainUser(UserEntity entity) {
        if (entity == null) return null;
        Set<Role> roles = entity.getRoles().stream()
                .map(this::toDomainRole)
                .collect(Collectors.toSet());
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                UserStatus.valueOf(entity.getStatus()),
                roles
        );
    }

    public UserEntity toEntityUser(User domain) {
        if (domain == null) return null;
        Set<RoleEntity> roleEntities = domain.getRoles().stream()
                .map(this::toEntityRole)
                .collect(Collectors.toSet());
        return new UserEntity(
                domain.getId(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getStatus().name(),
                roleEntities
        );
    }

    public RefreshToken toDomainRefreshToken(RefreshTokenEntity entity) {
        if (entity == null) return null;
        return new RefreshToken(
                entity.getId(),
                entity.getUserId(),
                entity.getToken(),
                entity.getExpiresAt(),
                entity.isRevoked()
        );
    }

    public RefreshTokenEntity toEntityRefreshToken(RefreshToken domain) {
        if (domain == null) return null;
        return new RefreshTokenEntity(
                domain.getId(),
                domain.getUserId(),
                domain.getToken(),
                domain.getExpiresAt(),
                domain.isRevoked()
        );
    }
}
