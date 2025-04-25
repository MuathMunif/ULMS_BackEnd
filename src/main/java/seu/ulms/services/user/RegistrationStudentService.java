package seu.ulms.services.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seu.ulms.dto.user.RegistrationStudentDto;
import seu.ulms.dto.user.UserDto;
import seu.ulms.entities.user.EUserRole;
import seu.ulms.entities.user.UserEntity;
import seu.ulms.mapper.user.UserMapper;
import seu.ulms.repositoies.universty.UniversityRepository;
import seu.ulms.repositoies.user.UserRepository;
import seu.ulms.services.keycloak.EKeycloakRole;
import seu.ulms.services.keycloak.KeycloakAdminService;

@Service
@RequiredArgsConstructor
public class RegistrationStudentService {
    private final UniversityRepository universityRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public RegistrationStudentDto registerStudent(RegistrationStudentDto studentDto) {
        if (userRepository.findByUsername(studentDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered!");
        }

        UserEntity user = userMapper.toEntity(studentDto);
        user.setUserRole(EUserRole.STUDENT);
        userRepository.save(user);

        UserDto userDto = userMapper.toDto(user);
        keycloakAdminService.createUser(userDto);
        keycloakAdminService.assignRoleToUser(userDto.getUsername(), EKeycloakRole.ROLE_STUDENT.toString());
        keycloakAdminService.triggerResetPasswordEmail(userDto.getUsername());

        return studentDto;
    }
}