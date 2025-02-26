package seu.ulms.services.university;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import seu.ulms.dto.university.UniversityDto;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.repositoies.universty.UniversityRepository;
import seu.ulms.mapper.UniversityMapper;
import seu.ulms.dto.UniversityCreationDto;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityMapper universityMapper;
    private final UniversityRepository universityRepository;

    // ✅ استرجاع الجامعات مع الـ Pagination باستخدام DTO
    public Page<UniversityDto> getAllUniversities(Pageable pageable) {
        return universityRepository.findAll(pageable)
                .map(universityMapper::toDto); // ✅ لا داعي لتعريف متغير جديد
    }

    // ✅ إنشاء جامعة جديدة وإرجاعها كـ DTO
    public UniversityDto createUniversity(UniversityCreationDto university) {
        UniversityEntity universityEntity = universityMapper.toEntity(university);
        return universityMapper.toDto(universityRepository.save(universityEntity));
    }

    // ✅ استرجاع جميع الجامعات كـ List<UniversityDto> بدون Pagination
    public List<UniversityDto> getAllUniversitiesList() {
        return universityRepository.findAll()
                .stream()
                .map(universityMapper::toDto)
                .collect(Collectors.toList());
    }

    // ✅ حذف جامعة عبر الـ ID
    public void deleteUniversity(Long id) {
        universityRepository.deleteById(id);
    }
}

