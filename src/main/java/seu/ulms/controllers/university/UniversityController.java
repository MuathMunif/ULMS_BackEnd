package seu.ulms.controllers.university;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.university.UniversityCreationDto;
import seu.ulms.dto.university.UniversityDto;
import seu.ulms.services.university.UniversityService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    //  يسمح فقط للأدمن بإنشاء جامعة جديدة
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<UniversityDto> createUniversity(@RequestBody @Valid UniversityCreationDto university) {
        return ResponseEntity.ok(universityService.createUniversity(university));
    }

    // يسمح للجميع برؤية الجامعات مع دعم Pagination
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE', 'STUDENT')")
    @GetMapping
    public ResponseEntity<Page<UniversityDto>> getAllUniversities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable) {
        Page<UniversityDto> universities = universityService.getAllUniversities(pageable);
        return ResponseEntity.ok(universities);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "University deleted successfully.");

        return ResponseEntity.ok(response);  // 200 OK with message
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("edit/{id}")
    public ResponseEntity<UniversityDto> updateUniversity(@PathVariable Long id, @RequestBody @Valid UniversityCreationDto dto) {
        return ResponseEntity.ok(universityService.updateUniversity(id, dto));
    }

}
