package springboot.application.security.dto;

import springboot.domain.security.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserResponse(
        String id,
        String email,
        Set<RoleResponse> roles,
        String status
) {
    public static UserResponse fromDomain(User user) {
        Set<RoleResponse> roleResponses = user.getRoles().stream()
                .map(r -> new RoleResponse(r.getId(), r.getName(), r.getAuthority()))
                .collect(Collectors.toSet());
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                roleResponses,
                user.getStatus().name()
        );
    }
}
