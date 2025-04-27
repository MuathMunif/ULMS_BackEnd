package seu.ulms.dto.university;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.universty.ERelationType;
import seu.ulms.entities.universty.EStatus;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UniversityRepresentativeDto {
    private Long id;
    private String fullName;
    private String email;
    private ERelationType relationType;
    private EStatus status;
}
