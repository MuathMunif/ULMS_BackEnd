package seu.ulms.controllers.university;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.university.AccessUniversityDto;
import seu.ulms.dto.university.AccessUniversityPostDto;
import seu.ulms.entities.universty.AccessUniversityEntity;
import seu.ulms.services.university.AccessUniversityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accessUniversity")
public class AccessUniversityController {
    private final AccessUniversityService accessUniversityService;

//    Create to allow admin to create new
//    Representative for certain University and create it in keyclok with rest password

    @PostMapping("/crerate_representative")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public AccessUniversityPostDto createRepresentative(@RequestBody AccessUniversityPostDto accessUniversityPostDto){
        return accessUniversityService.createRepresentative(accessUniversityPostDto);
    }
//    Create api to allow student request to register with  University so that the Representative approve or reject this request

    @PostMapping("/student/{universityId}")
    @PreAuthorize("hasAnyRole('STUDNET','ROLE_STUDNET','STUDENT','ROLE_STUDENT')")
    public AccessUniversityDto requestAccessUniversity(@PathVariable("universityId") Long universityId){
        return accessUniversityService.requestAccessUniversity(universityId);
    }

// Create API to allow Representative approve or reject the request

    @PostMapping("/update_status")
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE')")
    public AccessUniversityDto updateStatus(@RequestBody AccessUniversityDto accessUniversityDto){
        return accessUniversityService.updateStatus(accessUniversityDto);
    }

    // داله لحذف ممثل الجامعه
    @DeleteMapping("/delete/representative/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRepresentative(@PathVariable Long id) {
        accessUniversityService.deleteRepresentative(id);
        return ResponseEntity.ok("Representative deleted successfully.");
    }
}
