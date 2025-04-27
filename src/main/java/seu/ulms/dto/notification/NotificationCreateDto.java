package seu.ulms.dto.notification;

import lombok.*;
import seu.ulms.entities.notification.ENotificationType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationCreateDto {
    private Long userId;
    private String title;
    private String message;
    private ENotificationType type;
    private Long bookId;
}
