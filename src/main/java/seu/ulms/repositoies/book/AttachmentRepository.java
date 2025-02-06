package seu.ulms.repositoies.book;

import org.springframework.data.jpa.repository.JpaRepository;
import seu.ulms.entities.book.AttachmentEntity;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity,Long> {
}
