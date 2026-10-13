package springboot.application.security.usecase;

import springboot.application.security.dto.CreateUserRequest;
import springboot.application.security.dto.UserResponse;
import springboot.domain.security.model.Role;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.RoleRepository;
import springboot.domain.security.port.repository.UserRepository;
import springboot.domain.security.port.service.PasswordService;

public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;

    public CreateUserUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordService passwordService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordService = passwordService;
    }

    public UserResponse execute(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El correo electrónico ya se encuentra registrado: " + request.email());
        }

        String passwordHash = passwordService.hash(request.password());
        Role defaultRole = roleRepository.findByName("USER")
                .or(() -> roleRepository.findByAuthority("ROLE_USER"))
                .orElseGet(() -> roleRepository.save(Role.create("USER")));

        User user = User.create(request.email(), passwordHash, defaultRole);
        User savedUser = userRepository.save(user);

        return UserResponse.fromDomain(savedUser);
    }
}
