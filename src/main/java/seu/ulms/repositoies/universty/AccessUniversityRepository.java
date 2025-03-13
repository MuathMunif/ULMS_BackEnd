package seu.ulms.repositoies.universty;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.universty.AccessUniversityEntity;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.entities.user.UserEntity;

import java.util.Optional;

public interface AccessUniversityRepository extends JpaRepository<AccessUniversityEntity,Long> {

    Optional<AccessUniversityEntity> findByUserAndUniversity(UserEntity user, UniversityEntity university);
}
