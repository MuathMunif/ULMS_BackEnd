package seu.ulms.controllers.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.user.RegistrationStudentDto;
import seu.ulms.services.user.RegistrationStudentService;


@RestController
@RequestMapping("/public/students")
@RequiredArgsConstructor
public class RegistrationStudentController {

    private final RegistrationStudentService registrationStudentService;

    //  Endpoint عام لتسجيل الطلاب
    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestBody @Valid RegistrationStudentDto studentDto) {
        return ResponseEntity.ok(registrationStudentService.registerStudent(studentDto).getUsername() + " registered successfully!");
    }
}
