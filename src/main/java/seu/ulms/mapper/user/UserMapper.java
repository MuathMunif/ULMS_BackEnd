package seu.ulms.mapper.user;

import seu.ulms.dto.user.UserDto;
import seu.ulms.entities.user.UserEntity;

public class UserMapper {
    public static UserDto toDTO(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    public static UserEntity toEntity(UserDto userDTO) {
        UserEntity user = new UserEntity();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setUserRole(userDTO.getUserRole());
        return user;
    }
}
