package seu.ulms.services.book;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seu.ulms.entities.book.AttachmentEntity;
import seu.ulms.entities.book.EFileType;
import seu.ulms.repositoies.book.AttachmentRepository;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final MinioClient minioClient;
    private final AttachmentRepository attachmentRepository;

    @Value("${minio.bucket}")
    private String bucketName;

    //  رفع مرفق إلى MinIO وتخزين معلوماته في DB
    public AttachmentEntity uploadAttachment(MultipartFile file) {
        try {
            // تحقق من وجود الباكت أو إنشاؤه
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            InputStream stream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uniqueFileName)
                            .stream(stream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            AttachmentEntity attachment = new AttachmentEntity();
            attachment.setFileName(uniqueFileName);
            attachment.setFilePath(bucketName + "/" + uniqueFileName);
            attachment.setFileType(EFileType.valueOf(file.getContentType().toUpperCase().replace("/", "_")));
            attachment.setFileSize(file.getSize());

            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    //  جلب مرفق واحد من DB
    public AttachmentEntity getAttachmentById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
    }

    //  البحث باستخدام اسم الملف (من DB)
    public Optional<AttachmentEntity> getAttachmentByFileName(String fileName) {
        return attachmentRepository.findByFileName(fileName);
    }

    //  جلب كل المرفقات من DB
    public List<AttachmentEntity> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    //  تنزيل ملف من MinIO
    public ResponseEntity<Resource> downloadAttachment(Long id) {
        AttachmentEntity attachment = getAttachmentById(id);
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(attachment.getFileName())
                            .build()
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                    .body(new InputStreamResource(stream));

        } catch (Exception e) {
            throw new RuntimeException("Failed to download file from MinIO", e);
        }
    }

    //  حذف ملف من MinIO و DB
    public void deleteAttachment(Long id) {
        AttachmentEntity attachment = getAttachmentById(id);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(attachment.getFileName())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from MinIO", e);
        }

        attachmentRepository.deleteById(id);
    }
}







//package seu.ulms.services.book;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import seu.ulms.entities.book.AttachmentEntity;
//import seu.ulms.entities.book.EFileType;
//import seu.ulms.repositoies.book.AttachmentRepository;
//
//import java.io.IOException;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class AttachmentService {
//    private final AttachmentRepository attachmentRepository;
//
//    //  رفع مرفق جديد
//    public AttachmentEntity uploadAttachment(MultipartFile file) {
//        try {
//            AttachmentEntity attachment = new AttachmentEntity();
//            attachment.setFileName(file.getOriginalFilename());
//
//            String fileTypeString = file.getContentType().toUpperCase().replace("/", "_");
//            attachment.setFileType(EFileType.valueOf(fileTypeString));
//            attachment.setData(file.getBytes());
//            return attachmentRepository.save(attachment);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to upload file", e);
//        } catch (IllegalArgumentException e) {
//            throw new RuntimeException("Unsupported file type: " + file.getContentType(), e);
//        }
//    }
//
//    //  جلب مرفق عبر ID
//    public AttachmentEntity getAttachmentById(Long id) {
//        return attachmentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Attachment not found"));
//    }
//
//    //  البحث عن مرفق باستخدام `fileName`
//    public Optional<AttachmentEntity> getAttachmentByFileName(String fileName) {
//        return attachmentRepository.findByFileName(fileName);
//    }
//
//    //  تنزيل المرفق كملف
//    public ResponseEntity<Resource> downloadAttachment(Long id) {
//        AttachmentEntity attachment = getAttachmentById(id);
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(attachment.getFileType().toString().replace("_", "/")))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
//                .body(new ByteArrayResource(attachment.getData()));
//    }
//
//    //  حذف مرفق عبر ID
//    public void deleteAttachment(Long id) {
//        if (!attachmentRepository.existsById(id)) {
//            throw new RuntimeException("Attachment not found");
//        }
//        attachmentRepository.deleteById(id);
//    }
//}