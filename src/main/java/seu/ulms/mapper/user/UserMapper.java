package seu.ulms.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import seu.ulms.dto.user.RegistrationStudentDto;
import seu.ulms.dto.user.UserDto;
import seu.ulms.entities.user.UserEntity;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserDto userDto);
    UserDto toDto(UserEntity userEntity);

    UserEntity toEntity(RegistrationStudentDto registrationStudentDto);
}
