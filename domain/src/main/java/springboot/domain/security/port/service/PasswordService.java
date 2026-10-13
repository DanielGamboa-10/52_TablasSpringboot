package springboot.domain.security.port.service;

public interface PasswordService {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
