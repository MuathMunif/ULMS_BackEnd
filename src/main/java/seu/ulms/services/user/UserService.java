package seu.ulms.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.ulms.entities.user.UserEntity;
import seu.ulms.repositoies.user.UserRepository;

import java.util.List;

@Service
public class UserService {
@Autowired
    private UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
