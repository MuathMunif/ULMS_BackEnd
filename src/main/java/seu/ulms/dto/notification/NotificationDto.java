package seu.ulms.dto.notification;

import lombok.*;
import seu.ulms.entities.notification.ENotificationType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private ENotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long bookId;
    private Long userId;
}
