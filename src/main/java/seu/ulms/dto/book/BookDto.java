package seu.ulms.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookDto {
    private Long id;
    private String title;
    private String description;
    private String author;
    private LocalDate publishDate;
    private String version;
    private Long universityId;
    private String universityName;
    private String categoryName;
    private Long attachmentId;
}
