package seu.ulms.mapper.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.book.BookDto;
import seu.ulms.dto.book.PublicBookDto;
import seu.ulms.entities.book.BookEntity;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "university.name", target = "universityName")
    @Mapping(source = "university.id", target = "universityId")
    @Mapping(source = "category.title", target = "categoryName")
    BookDto toDto(BookEntity bookEntity);

    @Mapping(source = "university.name", target = "universityName")
    @Mapping(source = "category.title", target = "categoryName")
    @Mapping(source = "university.id", target = "universityId")
    PublicBookDto toPublicBookDto(BookEntity bookEntity);

    List<BookDto> toDtoList(List<BookEntity> bookEntities);

    BookEntity toEntity(BookDto bookDto);

    List<PublicBookDto> toPublicDtoList(List<BookEntity> bookEntities);
}