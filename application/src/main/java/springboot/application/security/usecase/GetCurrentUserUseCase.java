package springboot.application.security.usecase;

import springboot.application.security.dto.UserResponse;
import springboot.domain.security.exception.UserNotFoundException;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.UserRepository;

public class GetCurrentUserUseCase {
    private final UserRepository userRepository;

    public GetCurrentUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse execute(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
        return UserResponse.fromDomain(user);
    }
}
