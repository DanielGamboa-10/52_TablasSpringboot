package springboot.domain.security.port.repository;

import springboot.domain.security.model.Role;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(String id);
    Optional<Role> findByName(String name);
    Optional<Role> findByAuthority(String authority);
}
