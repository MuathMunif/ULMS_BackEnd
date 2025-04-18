package seu.ulms.dto.book;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDto {
    private Long id;
    private String title;
    private String description;
    private Long universityId;
}
