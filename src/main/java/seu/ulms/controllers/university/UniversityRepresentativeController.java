package seu.ulms.controllers.university;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.university.RepresentativeInfoDto;
import seu.ulms.services.university.RepresentativeService;


import java.util.List;

@RestController
@RequestMapping("/representatives")
@RequiredArgsConstructor
public class UniversityRepresentativeController {

    private final RepresentativeService representativeService;

    // جلب كل ممثلي الجامعات مع معلومات الجامعة
    @GetMapping("/AllRepresentatives")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RepresentativeInfoDto>> getAllRepresentatives() {
        return ResponseEntity.ok(representativeService.getAllRepresentativesWithUniversity());
    }
}
