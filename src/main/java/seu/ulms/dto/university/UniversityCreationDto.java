package seu.ulms.dto.university;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UniversityCreationDto {
    @NotEmpty
    @Size(min = 3, max = 50)
    private String name;
    @Email
    private String email;
    @NotEmpty
    private String domain;
    @Size(max = 50)
    private String location;
    @Size(max = 50)
    private String libraryName;
}
