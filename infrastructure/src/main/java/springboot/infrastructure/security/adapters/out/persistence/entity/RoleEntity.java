package springboot.infrastructure.security.adapters.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "authority", length = 50, nullable = false, unique = true)
    private String authority;

    public RoleEntity() {
    }

    public RoleEntity(String id, String name, String authority) {
        this.id = id;
        this.name = name;
        this.authority = authority;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthority() { return authority; }
    public void setAuthority(String authority) { this.authority = authority; }
}
