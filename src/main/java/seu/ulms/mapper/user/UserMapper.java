package seu.ulms.mapper.user;

import org.mapstruct.Mapper;
import seu.ulms.dto.user.UserDto;
import seu.ulms.entities.user.UserEntity;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserDto userDto);
    UserDto toDto(UserEntity userEntity);
}
