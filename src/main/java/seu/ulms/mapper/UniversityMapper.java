package seu.ulms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.university.UniversityCreationDto;
import seu.ulms.dto.university.UniversityDto;
import seu.ulms.entities.universty.UniversityEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccessUniversityMapper.class}) //  تضمين AccessUniversityMapper
public interface UniversityMapper {

    UniversityEntity toEntity(UniversityCreationDto universityCreationDto);

    @Mapping(source = "accessUniversity", target = "representative") //  ربط بيانات الممثل بـ UniversityDto
    UniversityDto toDto(UniversityEntity universityEntity);

    List<UniversityDto> toDtoList(List<UniversityEntity> universityEntities);
}
