package springboot.infrastructure.security.adapters.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.infrastructure.security.adapters.out.persistence.entity.RoleEntity;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByName(String name);
    Optional<RoleEntity> findByAuthority(String authority);
}
