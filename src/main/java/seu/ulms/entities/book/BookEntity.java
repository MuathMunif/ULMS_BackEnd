package seu.ulms.entities.book;

import jakarta.persistence.*;
import lombok.*;
import seu.ulms.entities.BasesAudit;
import seu.ulms.entities.universty.UniversityEntity;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookEntity extends BasesAudit implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_sq"
    )
    @SequenceGenerator(
            name = "book_sq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "version")
    private String version;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    private AttachmentEntity attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "universty_id")
    private UniversityEntity university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
