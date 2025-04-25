package seu.ulms.mapper.university;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.university.RepresentativeInfoDto;
import seu.ulms.entities.universty.AccessUniversityEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RepresentativeInfoMapper {

    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "relationType", target = "relationType")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "university.name", target = "universityName")
    RepresentativeInfoDto toDto(AccessUniversityEntity entity);

    List<RepresentativeInfoDto> toDtoList(List<AccessUniversityEntity> entities);
}
