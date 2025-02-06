package seu.ulms.repositoies.book;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.book.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
