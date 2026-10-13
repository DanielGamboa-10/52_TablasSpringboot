package springboot.domain.security.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserSuccessfully() {
        Role role = Role.create("USER");
        User user = User.create("test@mindconnect.com", "hash123", role);

        assertNotNull(user.getId());
        assertEquals("test@mindconnect.com", user.getEmail());
        assertEquals("hash123", user.getPassword());
        assertTrue(user.isActive());
        assertTrue(user.hasRole(role));
    }

    @Test
    void shouldAssignAndRemoveRole() {
        Role userRole = Role.create("USER");
        Role adminRole = Role.create("ADMIN");
        User user = User.create("admin@mindconnect.com", "hash123", userRole);

        user.assignRole(adminRole);
        assertTrue(user.hasRole(adminRole));
        assertEquals(2, user.getRoles().size());

        user.removeRole(adminRole);
        assertFalse(user.hasRole(adminRole));
        assertEquals(1, user.getRoles().size());
    }

    @Test
    void shouldBlockAndActivateUser() {
        User user = User.create("test@mindconnect.com", "hash123", Role.create("USER"));

        assertTrue(user.isActive());
        user.block();
        assertFalse(user.isActive());
        assertEquals(UserStatus.BLOCKED, user.getStatus());

        user.activate();
        assertTrue(user.isActive());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void shouldRejectInvalidEmail() {
        Role role = Role.create("USER");
        assertThrows(IllegalArgumentException.class, () -> User.create("invalid-email", "hash", role));
        assertThrows(IllegalArgumentException.class, () -> User.create(null, "hash", role));
        assertThrows(IllegalArgumentException.class, () -> User.create("", "hash", role));
    }

    @Test
    void shouldChangePassword() {
        User user = User.create("test@mindconnect.com", "hash123", Role.create("USER"));
        user.changePassword("newHash456");
        assertEquals("newHash456", user.getPassword());
    }
}
