package seu.ulms.repositoies.user;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
