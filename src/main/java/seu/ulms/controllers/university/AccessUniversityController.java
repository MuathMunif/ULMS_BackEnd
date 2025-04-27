package seu.ulms.controllers.university;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.university.AccessUniversityDto;
import seu.ulms.dto.university.AccessUniversityPostDto;
import seu.ulms.dto.university.UniversityAccessRequestDto;
import seu.ulms.entities.universty.AccessUniversityEntity;
import seu.ulms.services.university.AccessUniversityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accessUniversity")
public class AccessUniversityController {
    private final AccessUniversityService accessUniversityService;



    @PostMapping("/crerate_representative")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public AccessUniversityPostDto createRepresentative(@RequestBody AccessUniversityPostDto accessUniversityPostDto){
        return accessUniversityService.createRepresentative(accessUniversityPostDto);
    }


    @PostMapping("/student/{universityId}")
    @PreAuthorize("hasAnyRole('STUDNET','ROLE_STUDNET','STUDENT','ROLE_STUDENT')")
    public AccessUniversityDto requestAccessUniversity(@PathVariable("universityId") Long universityId){
        return accessUniversityService.requestAccessUniversity(universityId);
    }



    @PostMapping("/update_status")
    @PreAuthorize("isAuthenticated()")
    public AccessUniversityDto updateStatus(@RequestBody AccessUniversityDto accessUniversityDto){
        return accessUniversityService.updateStatus(accessUniversityDto);
    }

    //  لحذف ممثل الجامعه
    @DeleteMapping("/delete/representative/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRepresentative(@PathVariable Long id) {
        accessUniversityService.deleteRepresentative(id);
        return ResponseEntity.ok("Representative deleted successfully.");
    }

    //  لعرض الطلبات
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pending-requests")
    public ResponseEntity<List<UniversityAccessRequestDto>> getPendingRequests() {
        return ResponseEntity.ok(accessUniversityService.getPendingRequestsForRepresentative());
    }
}
