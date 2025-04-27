package seu.ulms.services.university;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.ulms.dto.university.RepresentativeInfoDto;
import seu.ulms.entities.universty.AccessUniversityEntity;
import seu.ulms.entities.universty.ERelationType;
import seu.ulms.mapper.university.RepresentativeInfoMapper;
import seu.ulms.repositoies.universty.AccessUniversityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepresentativeService {

    private final AccessUniversityRepository accessUniversityRepository;
    private final RepresentativeInfoMapper representativeInfoMapper;

    public List<RepresentativeInfoDto> getAllRepresentativesWithUniversity() {
        List<AccessUniversityEntity> representatives = accessUniversityRepository.findByRelationType(ERelationType.REPRESENTATIVE);
        return representatives.stream()
                .map(representativeInfoMapper::toDto)
                .collect(Collectors.toList());
    }
}
