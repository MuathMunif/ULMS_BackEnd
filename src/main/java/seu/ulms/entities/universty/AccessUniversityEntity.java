package seu.ulms.entities.universty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.BasesAudit;
import seu.ulms.entities.user.UserEntity;

import java.io.Serializable;

@Entity
@Table(name = "access_university")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccessUniversityEntity extends BasesAudit implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "acc_sq"
    )
    @SequenceGenerator(
            name = "acc_sq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "university_id",unique = false,nullable = false)
    private UniversityEntity university;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type")
    private ERelationType relationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatus status;
}
