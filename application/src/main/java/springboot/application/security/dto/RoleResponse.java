package springboot.application.security.dto;

public record RoleResponse(
        String id,
        String name,
        String authority
) {
}
