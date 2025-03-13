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
        //  التحقق مما إذا كان المستخدم موجودًا في النظام
        if (userRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already registered in the system!");
        }

        // حفظ بيانات الطالب في قاعدة البيانات
        UserEntity user = new UserEntity();
        user.setUsername(studentDto.getUsername());
        user.setFullName(studentDto.getFullName());
        user.setEmail(studentDto.getEmail());
        user.setUserRole(EUserRole.STUDENT);
        userRepository.save(user);

        //  تحويل المستخدم إلى `UserDto`
        UserDto userDto = userMapper.toDto(user);

        //  إنشاء المستخدم في Keycloak
        keycloakAdminService.createUser(userDto);

        //  إسناد دور الطالب للمستخدم
        keycloakAdminService.assignRoleToUser(userDto.getUsername(), EKeycloakRole.ROLE_STUDENT.toString());

        //  إرسال رابط إعادة تعيين كلمة المرور
        keycloakAdminService.triggerResetPasswordEmail(userDto.getUsername());

        return studentDto;
    }
}