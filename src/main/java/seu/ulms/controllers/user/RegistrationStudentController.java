package seu.ulms.controllers.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.user.RegistrationStudentDto;
import seu.ulms.services.keycloak.KeycloakAdminService;
import seu.ulms.services.user.RegistrationStudentService;


@RestController
@RequestMapping("/public/students")
@RequiredArgsConstructor
public class RegistrationStudentController {

    private final RegistrationStudentService registrationStudentService;
    private final KeycloakAdminService keycloakAdminService;

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;


    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(keycloak.realm(realm).users().list());
    }
    //  Endpoint عام لتسجيل الطلاب
//    @PostMapping("/register")
//    public ResponseEntity<String> registerStudent(@RequestBody @Valid RegistrationStudentDto studentDto) {
//        return ResponseEntity.ok(registrationStudentService.registerStudent(studentDto).getUsername() + " registered successfully!");
//    }
    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestBody @Valid RegistrationStudentDto studentDto) {
        registrationStudentService.registerStudent(studentDto);
        return ResponseEntity.ok("Student registered successfully!");
    }

    @GetMapping("/test-connection")
    public ResponseEntity<String> testConnection() {
        boolean connected = keycloakAdminService.isConnected();
        return ResponseEntity.ok(connected ? "Keycloak is connected!" : "Failed to connect to Keycloak");
    }


}
