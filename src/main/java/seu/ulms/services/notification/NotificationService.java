package seu.ulms.services.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seu.ulms.dto.notification.NotificationCreateDto;
import seu.ulms.dto.notification.NotificationDto;
import seu.ulms.entities.notification.NotificationEntity;
import seu.ulms.mapper.notification.NotificationMapper;
import seu.ulms.repositoies.notification.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    //  إنشاء إشعار جديد
    public NotificationDto create(NotificationCreateDto dto) {
        NotificationEntity entity = notificationMapper.toEntity(dto);
        return notificationMapper.toDto(notificationRepository.save(entity));
    }

    //  جلب جميع إشعارات مستخدم
    public List<NotificationDto> getAllByUser(Long userId) {
        return notificationRepository.findByUser_IdOrderByCreateAtDesc(userId)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    //  تحديث حالة القراءة
    @Transactional
    public void markAsRead(Long notificationId) {
        NotificationEntity entity = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        entity.setRead(true);
    }

    //  حذف الإشعار
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
