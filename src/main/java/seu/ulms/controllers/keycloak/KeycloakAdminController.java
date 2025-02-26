package seu.ulms.controllers.keycloak;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.user.UserDto;
import seu.ulms.services.keycloak.KeycloakAdminService;

@RestController
@RequestMapping("/api/keycloak")
@RequiredArgsConstructor
public class KeycloakAdminController {

    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        return keycloakAdminService.createUser(userDto);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return keycloakAdminService.getUser(username);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return keycloakAdminService.deleteUser(username);
    }

    @PostMapping("/users/{username}/roles/{roleName}")
    public ResponseEntity<String> assignRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        return keycloakAdminService.assignRoleToUser(username, roleName);
    }

    @PostMapping("/users/{username}/reset-password")
    public ResponseEntity<String> triggerResetPasswordEmail(@PathVariable String username) {
        return keycloakAdminService.triggerResetPasswordEmail(username);
    }

    @PostMapping("/users/{username}/verify-email")
    public ResponseEntity<String> triggerVerifyEmail(@PathVariable String username) {
        return keycloakAdminService.triggerVerifyEmail(username);
    }
}
