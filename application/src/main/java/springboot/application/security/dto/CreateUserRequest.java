package springboot.application.security.dto;

public record CreateUserRequest(
        String email,
        String password
) {
}
