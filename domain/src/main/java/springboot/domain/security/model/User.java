package springboot.domain.security.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class User {
    private final String id;
    private final String email;
    private String password;
    private UserStatus status;
    private final Set<Role> roles;

    public User(String id, String email, String password, UserStatus status, Set<Role> roles) {
        this.id = Objects.requireNonNullElseGet(id, () -> UUID.randomUUID().toString());
        this.email = validateEmail(email);
        this.password = Objects.requireNonNull(password, "La contraseña no puede ser nula");
        this.status = Objects.requireNonNullElse(status, UserStatus.ACTIVE);
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    public static User create(String email, String passwordHash, Role defaultRole) {
        Set<Role> initialRoles = new HashSet<>();
        if (defaultRole != null) {
            initialRoles.add(defaultRole);
        }
        return new User(UUID.randomUUID().toString(), email, passwordHash, UserStatus.ACTIVE, initialRoles);
    }

    private String validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido: " + email);
        }
        return email.trim().toLowerCase();
    }

    public void assignRole(Role role) {
        if (role != null) {
            this.roles.add(role);
        }
    }

    public void removeRole(Role role) {
        if (role != null) {
            this.roles.remove(role);
        }
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public void changePassword(String newPasswordHash) {
        this.password = Objects.requireNonNull(newPasswordHash, "La nueva contraseña no puede ser nula");
    }

    public void block() {
        this.status = UserStatus.BLOCKED;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserStatus getStatus() { return status; }
    public Set<Role> getRoles() { return new HashSet<>(roles); }
}
