package seu.ulms.repositoies.book;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.book.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
