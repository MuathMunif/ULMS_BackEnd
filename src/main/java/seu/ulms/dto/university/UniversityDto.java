package seu.ulms.dto.university;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.dto.university.UniversityRepresentativeDto;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class UniversityDto {
    private Long id;
    private String name;
    private String email;
    private String location;
    private String domain;
    private String libraryName;
    private UniversityRepresentativeDto representative;
}
