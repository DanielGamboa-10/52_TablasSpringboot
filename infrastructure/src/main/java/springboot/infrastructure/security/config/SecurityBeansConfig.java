package springboot.infrastructure.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.application.security.usecase.*;
import springboot.domain.security.port.repository.RefreshTokenRepository;
import springboot.domain.security.port.repository.RoleRepository;
import springboot.domain.security.port.repository.UserRepository;
import springboot.domain.security.port.service.PasswordService;
import springboot.domain.security.port.service.TokenService;

@Configuration
public class SecurityBeansConfig {

    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpirationMillis;

    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpirationMillis;

    @Bean
    public LoginUseCase loginUseCase(
            UserRepository userRepository,
            PasswordService passwordService,
            TokenService tokenService,
            RefreshTokenRepository refreshTokenRepository) {
        long accessTokenExpirationSeconds = accessTokenExpirationMillis / 1000;
        return new LoginUseCase(
                userRepository,
                passwordService,
                tokenService,
                refreshTokenRepository,
                refreshTokenExpirationMillis,
                accessTokenExpirationSeconds
        );
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            TokenService tokenService) {
        long accessTokenExpirationSeconds = accessTokenExpirationMillis / 1000;
        return new RefreshTokenUseCase(
                refreshTokenRepository,
                userRepository,
                tokenService,
                accessTokenExpirationSeconds
        );
    }

    @Bean
    public LogoutUseCase logoutUseCase(RefreshTokenRepository refreshTokenRepository) {
        return new LogoutUseCase(refreshTokenRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordService passwordService) {
        return new CreateUserUseCase(userRepository, roleRepository, passwordService);
    }

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepository userRepository) {
        return new GetCurrentUserUseCase(userRepository);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(
            UserRepository userRepository,
            PasswordService passwordService) {
        return new ChangePasswordUseCase(userRepository, passwordService);
    }

    @Bean
    public AssignRoleUseCase assignRoleUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository) {
        return new AssignRoleUseCase(userRepository, roleRepository);
    }
}
