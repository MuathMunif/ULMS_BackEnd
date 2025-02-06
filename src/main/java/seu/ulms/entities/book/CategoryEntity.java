package seu.ulms.entities.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.BasesAudit;
import seu.ulms.entities.universty.UniversityEntity;

import java.io.Serializable;

@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryEntity extends BasesAudit implements Serializable {
    @Id
    @GeneratedValue(
       strategy =  GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "category_id",
            allocationSize = 1
    )
    private Long id;
    @Column(name ="title",nullable = false)
    private String title;
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "university_id")
    private UniversityEntity university;


}
