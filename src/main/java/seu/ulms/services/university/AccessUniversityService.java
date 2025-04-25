package seu.ulms.services.university;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seu.ulms.dto.university.AccessUniversityDto;
import seu.ulms.dto.university.AccessUniversityPostDto;
import seu.ulms.dto.user.UserDto;
import seu.ulms.entities.universty.AccessUniversityEntity;
import seu.ulms.entities.universty.ERelationType;
import seu.ulms.entities.universty.EStatus;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.entities.user.EUserRole;
import seu.ulms.entities.user.UserEntity;

import seu.ulms.mapper.user.UserMapper;
import seu.ulms.repositoies.universty.AccessUniversityRepository;
import seu.ulms.repositoies.universty.UniversityRepository;
import seu.ulms.services.keycloak.EKeycloakRole;
import seu.ulms.services.keycloak.KeycloakAdminService;
import seu.ulms.services.user.UserService;
import seu.ulms.util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class AccessUniversityService {
    private final UniversityRepository universityRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final AccessUniversityRepository accessUniversityRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    @Transactional
    public AccessUniversityPostDto createRepresentative(AccessUniversityPostDto accessUniversityPostDto) {
        UniversityEntity universityEntity = universityRepository.findById(accessUniversityPostDto.getUniversityId())
                .orElseThrow(() -> new RuntimeException(" not found"));
        AccessUniversityEntity accessUniversityEntity = new AccessUniversityEntity();
        accessUniversityEntity.setUniversity(universityEntity);
        accessUniversityEntity.setStatus(EStatus.APPROVED);
        accessUniversityEntity.setRelationType(ERelationType.REPRESENTATIVE);

        UserEntity user = new UserEntity();
        user.setUsername(accessUniversityPostDto.getUsername());
        user.setFullName(accessUniversityPostDto.getFullName());
        user.setEmail(accessUniversityPostDto.getEmail());
        user.setUserRole(EUserRole.REPRESENTATIVE);
        accessUniversityEntity.setUser(user);
        accessUniversityRepository.save(accessUniversityEntity);
        UserDto userDto = userMapper.toDto(user);
        keycloakAdminService.createUser(userDto);
        keycloakAdminService.assignRoleToUser(userDto.getUsername(), EKeycloakRole.ROLE_UNIVERSITY_REPRESENTATIVE.toString());
        keycloakAdminService.triggerResetPasswordEmail(userDto.getUsername());
        return accessUniversityPostDto;
    }

    public AccessUniversityDto requestAccessUniversity(Long universityId) {
        UserEntity user = userService.syncUserWithKeycloak(SecurityUtil.getCurrentUserName());
        UniversityEntity universityEntity = universityRepository.findById(universityId).orElseThrow(() -> new RuntimeException("not found"));
        AccessUniversityEntity accessUniversityEntity =
                accessUniversityRepository.findByUserAndUniversity(user, universityEntity).orElse(new AccessUniversityEntity());
        accessUniversityEntity.setStatus(EStatus.PENDING);
        accessUniversityEntity.setUser(user);
        accessUniversityEntity.setRelationType(ERelationType.STUDENT);
        accessUniversityEntity.setUniversity(universityEntity);
        accessUniversityRepository.save(accessUniversityEntity);
        AccessUniversityDto accessUniversityDto = new AccessUniversityDto();
        accessUniversityDto.setId(accessUniversityEntity.getId());
        accessUniversityDto.setStatus(EStatus.PENDING);
        return accessUniversityDto;

    }

    public AccessUniversityDto updateStatus(AccessUniversityDto accessUniversityDto) {
        AccessUniversityEntity accessUniversityEntity = accessUniversityRepository
                .findById(accessUniversityDto.getId()).orElseThrow(() -> new RuntimeException("not found"));

        UserEntity user = userMapper.toEntity(userService.getUserByUsername(SecurityUtil.getCurrentUserName()));
        if (user.getUserRole() == EUserRole.REPRESENTATIVE) {
            // this method to check if a REPRESENTATIVE already has access in this University
            accessUniversityRepository.findByUserAndUniversity(user,
                    accessUniversityEntity.getUniversity()).orElseThrow(() -> new RuntimeException("not found"));
        }
        accessUniversityEntity.setStatus(accessUniversityDto.getStatus());
        accessUniversityRepository.save(accessUniversityEntity);
        return accessUniversityDto;
    }

    // تجربه ميثود لحذف ممثل
    @Transactional
    public void deleteRepresentative(Long accessId) {
        // 1. جلب العلاقة
        AccessUniversityEntity accessUniversityEntity = accessUniversityRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Representative link not found"));

        // 2. التحقق من أنه ممثل
        if (accessUniversityEntity.getRelationType() != ERelationType.REPRESENTATIVE) {
            throw new RuntimeException("This user is not a representative");
        }

        // 3. استخراج بيانات المستخدم
        UserEntity user = accessUniversityEntity.getUser();
        String username = user.getUsername();

        // 4. حذف العلاقة access_university
        accessUniversityRepository.deleteById(accessId);

        // 5. حذف المستخدم من قاعدة البيانات
        userService.deleteUser(user.getId());

        // 6. حذف المستخدم من Keycloak
        keycloakAdminService.deleteUser(username);
    }
}
