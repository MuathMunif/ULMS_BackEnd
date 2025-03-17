package seu.ulms.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.ulms.dto.user.UserDto;
import seu.ulms.entities.user.EUserRole;
import seu.ulms.entities.user.UserEntity;
import seu.ulms.mapper.user.UserMapper;
import seu.ulms.repositoies.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDTO) {
        UserEntity userEntity = userMapper.toEntity(userDTO);
        // TODO check if user exsist ?
        userEntity.setUserRole(EUserRole.STUDENT);
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public UserDto getUserByUsername(String username) {
        return userMapper.toDto(userRepository.findByUsername(username).orElseThrow(RuntimeException::new));
    }
}
