package seu.ulms.repositoies.book;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.book.BookEntity;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {


    List<BookEntity> findAllByUniversityId(Long universityId);


    List<BookEntity> findByTitleContainingIgnoreCase(String title);


    List<BookEntity> findByAuthorContainingIgnoreCase(String author);


    List<BookEntity> findByPublishDateAfter(LocalDate publishDate);
}
