package seu.ulms.repositoies.universty;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import seu.ulms.entities.universty.UniversityEntity;

public interface UniversityRepository extends JpaRepository<UniversityEntity,Long> {
    Page<UniversityEntity> findAll(Pageable pageable);
}
