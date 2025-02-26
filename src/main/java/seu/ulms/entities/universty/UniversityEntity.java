package seu.ulms.entities.universty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.BasesAudit;
import seu.ulms.entities.book.BookEntity;
import seu.ulms.entities.book.CategoryEntity;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "university")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UniversityEntity extends BasesAudit implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "uni_sq"
    )
    @SequenceGenerator(
            name = "uni_sq",
            allocationSize = 1
    )
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "domain", nullable = false, unique = true)
    private String domain;
    @Column(name = "location")
    private String location;
    @Column(name = "library_name", nullable = false)
    private String libraryName;

    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private List<CategoryEntity> categoryList;

    @OneToMany(mappedBy = "university")
    private List<BookEntity> bookList;

    // اضافه من عندي

    @OneToOne(mappedBy = "university")
    private AccessUniversityEntity accessUniversity;
}
