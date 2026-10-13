package springboot.domain.security.model;

import java.util.Objects;
import java.util.UUID;

public class Role {
    private final String id;
    private final String name;
    private final String authority;

    public Role(String id, String name, String authority) {
        this.id = Objects.requireNonNullElseGet(id, () -> UUID.randomUUID().toString());
        this.name = Objects.requireNonNull(name, "El nombre del rol no puede ser nulo");
        this.authority = authority != null ? authority : "ROLE_" + name;
    }

    public static Role create(String name) {
        return new Role(UUID.randomUUID().toString(), name.toUpperCase(), "ROLE_" + name.toUpperCase());
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAuthority() { return authority; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
