package seu.ulms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.services.university.UniversityService;

import java.util.List;

@RestController
@RequestMapping("/universities")
public class UniversityController {
    @Autowired
    private UniversityService universityService;

    @PostMapping
     public ResponseEntity<UniversityEntity> createUniversity(@RequestBody UniversityEntity university) {
            UniversityEntity createdUniversity = universityService.createUniversity(university);
            return ResponseEntity.ok(createdUniversity);
        }

    @GetMapping
    public ResponseEntity<List<UniversityEntity>> getAllUniversities() {
        List<UniversityEntity> universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
