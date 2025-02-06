package seu.ulms.entities.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.BasesAudit;

import java.io.Serializable;

@Entity
@Table(name = "attachment")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AttachmentEntity extends BasesAudit implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attach_sq"
    )
    @SequenceGenerator(
            name = "attach_sq",
            allocationSize = 1
    )
    private long id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private EFileType fileType;
    @Column(name = "file_size")
    private String fileSize;
}
