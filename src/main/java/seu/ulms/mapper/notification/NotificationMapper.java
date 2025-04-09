package seu.ulms.mapper.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import seu.ulms.dto.notification.NotificationCreateDto;
import seu.ulms.dto.notification.NotificationDto;
import seu.ulms.entities.notification.NotificationEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "read", constant = "false") // لأن في الكيان اسم الحقل read
    NotificationEntity toEntity(NotificationCreateDto dto);

    @Mapping(source = "read", target = "isRead") // ربط read مع isRead
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "book.id", target = "bookId")
    NotificationDto toDto(NotificationEntity entity);

    List<NotificationDto> toDtoList(List<NotificationEntity> entities);
}
