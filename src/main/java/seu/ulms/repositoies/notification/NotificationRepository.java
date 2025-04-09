package seu.ulms.repositoies.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.notification.NotificationEntity;
import seu.ulms.entities.user.UserEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    //  الحصول على كل الإشعارات الخاصة بمستخدم معيّن
    List<NotificationEntity> findByUserOrderByCreateAtDesc(UserEntity user);

    //  الحصول على عدد الإشعارات غير المقروءة
    long countByUserAndReadIsFalse(UserEntity user);

    List<NotificationEntity> findByUser_IdOrderByCreateAtDesc(Long userId);
}
