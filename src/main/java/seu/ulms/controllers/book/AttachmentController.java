package seu.ulms.controllers.book;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seu.ulms.entities.book.AttachmentEntity;
import seu.ulms.services.book.AttachmentService;

import java.util.Optional;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    //  رفع مرفق جديد (يسمح فقط لممثل الجامعة)
    @PreAuthorize("hasRole('UNIVERSITY_REPRESENTATIVE')")
    @PostMapping("/upload")
    public ResponseEntity<AttachmentEntity> uploadAttachment(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachmentService.uploadAttachment(file));
    }

    //  جلب مرفق عبر ID (متاح للجميع)
    @GetMapping("/{id}")
    public ResponseEntity<AttachmentEntity> getAttachmentById(@PathVariable Long id) {
        return ResponseEntity.ok(attachmentService.getAttachmentById(id));
    }

    //  البحث عن مرفق باستخدام `fileName` (متاح للجميع)
    @GetMapping("/search")
    public ResponseEntity<Optional<AttachmentEntity>> getAttachmentByFileName(@RequestParam String fileName) {
        return ResponseEntity.ok(attachmentService.getAttachmentByFileName(fileName));
    }

    //  تنزيل المرفق كملف (متاح للجميع)
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) {
        return attachmentService.downloadAttachment(id);
    }

    //  حذف مرفق (يسمح للأدمن وممثل الجامعة)
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}
