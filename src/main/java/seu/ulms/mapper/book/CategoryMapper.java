package seu.ulms.mapper.book;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.book.CategoryDto;
import seu.ulms.entities.book.CategoryEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "university.id", target = "universityId")
    CategoryDto toDto(CategoryEntity categoryEntity);


    @Mapping(source = "universityId", target = "university.id")
    CategoryEntity toEntity(CategoryDto categoryDto);

    List<CategoryDto> toDtoList(List<CategoryEntity> entities);
}
