package springboot.infrastructure.security.adapters.out.persistence.adapter;

import org.springframework.stereotype.Component;
import springboot.domain.security.model.Role;
import springboot.domain.security.port.repository.RoleRepository;
import springboot.infrastructure.security.adapters.out.persistence.entity.RoleEntity;
import springboot.infrastructure.security.adapters.out.persistence.mappers.SecurityPersistenceMapper;
import springboot.infrastructure.security.adapters.out.persistence.repository.RoleJpaRepository;

import java.util.Optional;

@Component
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository jpaRepository;
    private final SecurityPersistenceMapper mapper;

    public RoleRepositoryAdapter(RoleJpaRepository jpaRepository, SecurityPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = mapper.toEntityRole(role);
        RoleEntity saved = jpaRepository.save(entity);
        return mapper.toDomainRole(saved);
    }

    @Override
    public Optional<Role> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomainRole);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toDomainRole);
    }

    @Override
    public Optional<Role> findByAuthority(String authority) {
        return jpaRepository.findByAuthority(authority).map(mapper::toDomainRole);
    }
}
