package seu.ulms.repositoies.book;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.book.BookEntity;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    //  جلب جميع الكتب الخاصة بجامعة معينة
    List<BookEntity> findAllByUniversityId(Long universityId);

    //  البحث عن كتاب يحتوي على عنوان معين (`LIKE %title%`)
    List<BookEntity> findByTitleContainingIgnoreCase(String title);

    //  جلب جميع الكتب بناءً على المؤلف (`LIKE %author%`)
    List<BookEntity> findByAuthorContainingIgnoreCase(String author);

    //  جلب جميع الكتب التي تم نشرها بعد تاريخ معين
    List<BookEntity> findByPublishDateAfter(LocalDate publishDate);
}
