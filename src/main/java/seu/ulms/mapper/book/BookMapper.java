package seu.ulms.mapper.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.book.BookDto;
import seu.ulms.entities.book.BookEntity;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "university.name", target = "universityName") // ✅ تحويل اسم الجامعة
    @Mapping(source = "category.title", target = "categoryName") // ✅ تحويل اسم التصنيف
    BookDto toDto(BookEntity bookEntity);

    List<BookDto> toDtoList(List<BookEntity> bookEntities);

    BookEntity toEntity(BookDto bookDto);
}