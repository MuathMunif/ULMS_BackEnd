package seu.ulms.dto.university;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seu.ulms.entities.universty.EStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UniversityAccessRequestDto {
    private Long id;             // ID للطلب
    private String studentName;   // اسم الطالب
    private String studentEmail;  // إيميل الطالب
    private EStatus status;       // حالة الطلب (Pending, Approved, Rejected)
}
