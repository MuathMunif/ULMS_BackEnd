package seu.ulms.services.university;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.repositoies.universty.UniversityRepository;

import java.util.List;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    public UniversityEntity createUniversity(UniversityEntity university) {
        return universityRepository.save(university);
    }

    public List<UniversityEntity> getAllUniversities() {
        return universityRepository.findAll();
    }

    public void deleteUniversity(Long id) {
        universityRepository.deleteById(id);
    }

}

