package seu.ulms.mapper.university;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.university.UniversityCreationDto;
import seu.ulms.dto.university.UniversityDto;
import seu.ulms.dto.university.UniversityRepresentativeDto;
import seu.ulms.entities.universty.AccessUniversityEntity;
import seu.ulms.entities.universty.ERelationType;
import seu.ulms.entities.universty.UniversityEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UniversityMapper {

    UniversityEntity toEntity(UniversityCreationDto dto);

    @Mapping(target = "representative", expression = "java(mapToRepresentative(universityEntity.getAccessUniversity()))")
    UniversityDto toDto(UniversityEntity universityEntity);

    List<UniversityDto> toDtoList(List<UniversityEntity> entities);

    // ✅ هذا هو المفتاح، ميثود مخصص لتحويل القائمة إلى ممثل واحد
    default UniversityRepresentativeDto mapToRepresentative(List<AccessUniversityEntity> accessList) {
        if (accessList == null) return null;

        return accessList.stream()
                .filter(a -> a.getRelationType() == ERelationType.REPRESENTATIVE)
                .findFirst()
                .map(a -> {
                    UniversityRepresentativeDto dto = new UniversityRepresentativeDto();
                    dto.setId(a.getId());
                    dto.setFullName(a.getUser().getFullName());
                    dto.setEmail(a.getUser().getEmail());
                    dto.setRelationType(a.getRelationType());
                    dto.setStatus(a.getStatus());
                    return dto;
                })
                .orElse(null);
    }
}
