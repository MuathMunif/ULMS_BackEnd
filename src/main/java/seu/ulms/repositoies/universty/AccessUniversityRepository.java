package seu.ulms.repositoies.universty;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.universty.AccessUniversityEntity;

public interface AccessUniversityRepository extends JpaRepository<AccessUniversityEntity,Long> {
}
