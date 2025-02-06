package seu.ulms.entities.user;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.BasesAudit;

import java.io.Serializable;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEntity extends BasesAudit implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sq"
    )
    @SequenceGenerator(
            name = "user_sq",
            allocationSize = 1
    )
    private Long id;
    @Column(name = "user_name", nullable = false, unique = true)
    private String username;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "email", nullable = false, unique = true)
    private  String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private EUserRole userRole;
}
