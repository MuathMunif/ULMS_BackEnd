package seu.ulms.controllers.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.notification.NotificationCreateDto;
import seu.ulms.dto.notification.NotificationDto;
import seu.ulms.services.notification.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    //  إنشاء إشعار جديد (مثلاً من مسؤول أو ممثل الجامعة)
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE')")
    @PostMapping
    public ResponseEntity<NotificationDto> create(@RequestBody NotificationCreateDto dto) {
        return ResponseEntity.ok(notificationService.create(dto));
    }

    //  جلب جميع إشعارات المستخدم
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE', 'STUDENT')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> getAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getAllByUser(userId));
    }

    //  تحديث حالة القراءة
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE', 'STUDENT')")
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    //  حذف إشعار
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}