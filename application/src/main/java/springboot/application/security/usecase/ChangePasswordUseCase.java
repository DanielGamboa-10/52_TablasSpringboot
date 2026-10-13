package springboot.application.security.usecase;

import springboot.application.security.dto.ChangePasswordRequest;
import springboot.domain.security.exception.InvalidCredentialsException;
import springboot.domain.security.exception.UserNotFoundException;
import springboot.domain.security.model.User;
import springboot.domain.security.port.repository.UserRepository;
import springboot.domain.security.port.service.PasswordService;

public class ChangePasswordUseCase {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public ChangePasswordUseCase(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public void execute(String userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));

        if (!passwordService.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("La contraseña actual es incorrecta");
        }

        String newPasswordHash = passwordService.hash(request.newPassword());
        user.changePassword(newPasswordHash);
        userRepository.save(user);
    }
}
