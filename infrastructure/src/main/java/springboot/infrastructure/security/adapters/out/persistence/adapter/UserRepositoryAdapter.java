package springboot.infrastructure.security.adapters.out.persistence.adapter;

import org.springframework.stereotype.Component;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.UserRepository;
import springboot.infrastructure.security.adapters.out.persistence.entity.UserEntity;
import springboot.infrastructure.security.adapters.out.persistence.mappers.SecurityPersistenceMapper;
import springboot.infrastructure.security.adapters.out.persistence.repository.UserJpaRepository;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final SecurityPersistenceMapper mapper;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository, SecurityPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntityUser(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomainUser(saved);
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomainUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomainUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
