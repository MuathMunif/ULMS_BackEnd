package seu.ulms.services.book;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import seu.ulms.dto.book.BookDto;
import seu.ulms.entities.book.BookEntity;
import seu.ulms.entities.book.AttachmentEntity;
import seu.ulms.entities.book.CategoryEntity;
import seu.ulms.entities.universty.AccessUniversityEntity;
import seu.ulms.entities.universty.EStatus;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.entities.user.UserEntity;
import seu.ulms.mapper.book.BookMapper;
import seu.ulms.repositoies.book.AttachmentRepository;
import seu.ulms.repositoies.book.BookRepository;
import seu.ulms.repositoies.book.CategoryRepository;
import seu.ulms.repositoies.universty.AccessUniversityRepository;
import seu.ulms.repositoies.universty.UniversityRepository;
import seu.ulms.services.user.UserService;
import seu.ulms.util.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UniversityRepository universityRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserService userService;
    private final AccessUniversityRepository accessUniversityRepository;
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    // إنشاء أو تحديث كتاب
    public BookDto createOrUpdateBook(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.toEntity(bookDto);

        // ربط الجامعة
        UniversityEntity university = universityRepository.findById(bookDto.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found with ID: " + bookDto.getUniversityId()));
        bookEntity.setUniversity(university);

        // ربط التصنيف
        if (bookDto.getCategoryName() != null) {
            CategoryEntity category = categoryRepository.findAll().stream()
                    .filter(c -> c.getTitle().equalsIgnoreCase(bookDto.getCategoryName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Category not found: " + bookDto.getCategoryName()));
            bookEntity.setCategory(category);
        }

        // ربط المرفق
        if (bookDto.getAttachmentId() != null) {
            AttachmentEntity attachment = attachmentRepository.findById(bookDto.getAttachmentId())
                    .orElseThrow(() -> new RuntimeException("Attachment not found with ID: " + bookDto.getAttachmentId()));
            bookEntity.setAttachment(attachment);
        }

        return bookMapper.toDto(bookRepository.save(bookEntity));
    }

    // جلب جميع الكتب مع Pagination
    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    // جلب جميع الكتب الخاصة بجامعة معينة
    public List<BookDto> getBooksByUniversity(Long universityId) {
        return bookRepository.findAllByUniversityId(universityId)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    // جلب كتاب باستخدام ID
    public BookDto getBookById(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
        return bookMapper.toDto(book);
    }

    // البحث عن كتاب حسب العنوان
    public List<BookDto> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    // حذف كتاب
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    // تحديث كتاب
    public BookDto updateBook(Long id, BookDto bookDetails) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setDescription(bookDetails.getDescription());
        book.setPublishDate(bookDetails.getPublishDate());
        book.setVersion(bookDetails.getVersion());

        if (bookDetails.getCategoryName() != null) {
            CategoryEntity category = categoryRepository.findAll().stream()
                    .filter(c -> c.getTitle().equalsIgnoreCase(bookDetails.getCategoryName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            book.setCategory(category);
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

  // عرض الكتب للمصرح لهم مع رابط لعرض الكتاب
    public List<BookDto> getBooksForStudentByUniversity(Long universityId) {
        UserEntity currentUser = userService.syncUserWithKeycloak(SecurityUtil.getCurrentUserName());

        AccessUniversityEntity accessUniversity = accessUniversityRepository
                .findByUserAndUniversity(currentUser, universityRepository.findById(universityId)
                        .orElseThrow(() -> new RuntimeException("University not found")))
                .orElseThrow(() -> new RuntimeException("You have not requested access to this university"));

        if (accessUniversity.getStatus() != EStatus.APPROVED) {
            throw new RuntimeException("Access Denied: Your request to this university is not approved yet.");
        }

        return bookRepository.findAllByUniversityId(universityId)
                .stream()
                .map(book -> {
                    BookDto bookDto = bookMapper.toDto(book);

                    // إذا كان فيه مرفق مربوط بالكتاب
                    if (book.getAttachment() != null) {
                        try {
                            String url = minioClient.getPresignedObjectUrl(
                                    GetPresignedObjectUrlArgs.builder()
                                            .method(Method.GET)
                                            .bucket(bucketName)
                                            .object(book.getAttachment().getFileName())
                                            .expiry(15 * 60) // 15 دقيقة
                                            .build()
                            );
                            bookDto.setFileUrl(url);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to generate file URL", e);
                        }
                    }

                    return bookDto;
                })
                .collect(Collectors.toList());
    }
}
