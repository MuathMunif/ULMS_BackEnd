package seu.ulms.repositoies.universty;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.universty.UniversityEntity;

public interface UniversityRepository extends JpaRepository<UniversityEntity,Long> {
}
