package springboot.application.security.usecase;

import springboot.application.security.dto.AssignRoleRequest;
import springboot.application.security.dto.UserResponse;
import springboot.domain.security.exception.UserNotFoundException;
import springboot.domain.security.model.Role;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.RoleRepository;
import springboot.domain.security.port.repository.UserRepository;

public class AssignRoleUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AssignRoleUseCase(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserResponse execute(String userId, AssignRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));

        String roleName = request.roleName().toUpperCase().trim();
        Role role = roleRepository.findByName(roleName)
                .or(() -> roleRepository.findByAuthority(roleName))
                .or(() -> roleRepository.findByAuthority("ROLE_" + roleName))
                .orElseGet(() -> roleRepository.save(Role.create(roleName)));

        user.assignRole(role);
        User savedUser = userRepository.save(user);

        return UserResponse.fromDomain(savedUser);
    }
}
