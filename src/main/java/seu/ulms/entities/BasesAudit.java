package seu.ulms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BasesAudit {
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @LastModifiedBy
    @Column(name ="updated_by")
    private String updatedBy;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted;
}
