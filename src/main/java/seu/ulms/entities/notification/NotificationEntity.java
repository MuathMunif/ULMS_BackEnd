package seu.ulms.entities.notification;

import jakarta.persistence.*;
import lombok.*;
import seu.ulms.entities.book.BookEntity;
import seu.ulms.entities.user.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity extends seu.ulms.entities.BasesAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ENotificationType type;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String message;
    @Column(name = "is_read")
    private boolean read = false;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

}
