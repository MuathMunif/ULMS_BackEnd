package seu.ulms.dto.university;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccessUniversityPostDto {

    private Long universityId;
    private String username;
    private String email;
    private String fullName;
}
