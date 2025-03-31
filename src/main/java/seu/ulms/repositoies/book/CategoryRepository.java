package seu.ulms.repositoies.book;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.book.CategoryEntity;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByTitleIgnoreCase(String title);

    List<CategoryEntity> findByTitleContainingIgnoreCase(String title);
}
