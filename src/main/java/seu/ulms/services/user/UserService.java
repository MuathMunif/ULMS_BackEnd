package seu.ulms.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.ulms.dto.user.UserDto;
import seu.ulms.entities.user.EUserRole;
import seu.ulms.entities.user.UserEntity;
import seu.ulms.mapper.user.UserMapper;
import seu.ulms.repositoies.user.UserRepository;
import seu.ulms.services.keycloak.KeycloakAdminService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakAdminService keycloakAdminService;

    // إنشاء مستخدم جديد
    public UserDto createUser(UserDto userDTO) {
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity.setUserRole(EUserRole.STUDENT);
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    // جلب جميع المستخدمين
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // حذف مستخدم بواسطة ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // جلب مستخدم بواسطة ID
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    // جلب مستخدم بواسطة اسم المستخدم
    public UserDto getUserByUsername(String username) {
        return userMapper.toDto(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
        );
    }

    // جلب الكيان الكامل للمستخدم بواسطة ID
    public UserEntity getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //  مزامنة مستخدم من Keycloak إذا لم يكن موجود في قاعدة البيانات
    public UserEntity syncUserWithKeycloak(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get(); //  ترجع كائن Managed
        }

        UserDto userDtoFromKeycloak = keycloakAdminService.getUserByUsername(username);
        if (userDtoFromKeycloak == null) {
            throw new RuntimeException("User not found in Keycloak: " + username);
        }

        UserEntity userEntity = userMapper.toEntity(userDtoFromKeycloak);
        userEntity.setUserRole(EUserRole.STUDENT);
        return userRepository.save(userEntity); //  حفظه يخليه Managed
    }
     // اضفت الداله هذي دايركت على user entity لكي استخدمها لجلب معلومات ممثل جامعه "اثناء تحديث حاله الطلبات"
     public UserEntity getUserEntityByUsername(String username) {
         return userRepository.findByUsername(username)
                 .orElseThrow(() -> new RuntimeException("User not found with username test: " + username));
     }
}