package seu.ulms.repositoies.book;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.book.AttachmentEntity;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    //  البحث عن مرفق معين باستخدام `fileName`
    Optional<AttachmentEntity> findByFileName(String fileName);
}
