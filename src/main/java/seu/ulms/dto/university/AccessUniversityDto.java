package seu.ulms.dto.university;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.universty.EStatus;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccessUniversityDto {
    private Long id;
    private EStatus status;
}
