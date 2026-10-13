package springboot.domain.security.port.service;

import springboot.domain.security.model.User;
import java.util.List;

public interface TokenService {
    String generateAccessToken(User user);
    String generateRefreshToken();
    String validateAccessToken(String token);
    List<String> extractRoles(String token);
    String extractEmail(String token);
}
