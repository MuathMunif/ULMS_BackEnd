package seu.ulms.mapper.university;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.university.UniversityAccessRequestDto;
import seu.ulms.dto.university.UniversityRepresentativeDto;
import seu.ulms.entities.universty.AccessUniversityEntity;

@Mapper(componentModel = "spring")
public interface AccessUniversityMapper {


    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email") // تحديد استخراج email
    @Mapping(source = "relationType", target = "relationType")
    @Mapping(source = "status", target = "status")
    UniversityRepresentativeDto toDto(AccessUniversityEntity entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.fullName", target = "studentName")
    @Mapping(source = "user.email", target = "studentEmail")
    @Mapping(source = "status", target = "status")
    UniversityAccessRequestDto toRequestDto(AccessUniversityEntity entity);
}
