
package softball.app.dto;

import softball.app.jpa.Role;
import softball.app.jpa.User;

public record LoginResponseDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        Role role,
        String token) {
    public static LoginResponseDTO from(User user, String token) {
        return new LoginResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                token);
    }
}
