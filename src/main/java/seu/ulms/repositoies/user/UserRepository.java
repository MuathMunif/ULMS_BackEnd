package seu.ulms.repositoies.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seu.ulms.entities.user.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
