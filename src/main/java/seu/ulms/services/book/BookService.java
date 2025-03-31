package seu.ulms.services.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import seu.ulms.dto.book.BookDto;
import seu.ulms.entities.book.BookEntity;
import seu.ulms.entities.book.AttachmentEntity;
import seu.ulms.entities.book.CategoryEntity;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.mapper.book.BookMapper;
import seu.ulms.repositoies.book.AttachmentRepository;
import seu.ulms.repositoies.book.BookRepository;
import seu.ulms.repositoies.book.CategoryRepository;
import seu.ulms.repositoies.universty.UniversityRepository;

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

        // تحديث الجامعة إذا تم تمرير ID جديد
        if (bookDetails.getUniversityId() != null) {
            UniversityEntity university = universityRepository.findById(bookDetails.getUniversityId())
                    .orElseThrow(() -> new RuntimeException("University not found"));
            book.setUniversity(university);
        }

        // تحديث التصنيف إذا تم تمرير اسم
        if (bookDetails.getCategoryName() != null) {
            CategoryEntity category = categoryRepository.findAll().stream()
                    .filter(c -> c.getTitle().equalsIgnoreCase(bookDetails.getCategoryName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            book.setCategory(category);
        }

        // تحديث المرفق إذا تم تمرير ID
        if (bookDetails.getAttachmentId() != null) {
            AttachmentEntity attachment = attachmentRepository.findById(bookDetails.getAttachmentId())
                    .orElseThrow(() -> new RuntimeException("Attachment not found"));
            book.setAttachment(attachment);
        }

        return bookMapper.toDto(bookRepository.save(book));
    }
}



//package seu.ulms.services.book;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import seu.ulms.dto.book.BookDto;
//import seu.ulms.entities.book.BookEntity;
//import seu.ulms.entities.universty.UniversityEntity;
//import seu.ulms.mapper.book.BookMapper;
//import seu.ulms.repositoies.book.BookRepository;
//import seu.ulms.repositoies.universty.UniversityRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class BookService {
//    private final BookRepository bookRepository;
//    private final BookMapper bookMapper;
//    private final UniversityRepository universityRepository;
//    //  إنشاء كتاب جديد
//    public BookDto createOrUpdateBook(BookDto bookDto) {
//        BookEntity bookEntity = bookMapper.toEntity(bookDto);
//       UniversityEntity university= universityRepository.findById(bookDto.getUniversityId())
//               .orElseThrow(()-> new RuntimeException("no university"));
//       bookEntity.setUniversity(university);
//        return bookMapper.toDto(bookRepository.save(bookEntity));
//    }
//
//    //  جلب جميع الكتب مع دعم `Pagination`
//    public Page<BookDto> getAllBooks(Pageable pageable) {
//        return bookRepository.findAll(pageable)
//                .map(bookMapper::toDto);
//    }
//
//    //  جلب جميع الكتب الخاصة بجامعة معينة
//    public List<BookDto> getBooksByUniversity(Long universityId) {
//        return bookRepository.findAllByUniversityId(universityId)
//                .stream()
//                .map(bookMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    //  جلب كتاب معين باستخدام `ID`
//    public BookDto getBookById(Long id) {
//        BookEntity book = bookRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Book not found"));
//        return bookMapper.toDto(book);
//    }
//
//    //  البحث عن كتاب حسب العنوان
//    public List<BookDto> searchBooksByTitle(String title) {
//        return bookRepository.findByTitleContainingIgnoreCase(title)
//                .stream()
//                .map(bookMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    //  حذف كتاب إذا كان موجودًا
//    public void deleteBook(Long id) {
//        if (!bookRepository.existsById(id)) {
//            throw new RuntimeException("Book not found");
//        }
//        bookRepository.deleteById(id);
//    }
//
//    //  تحديث بيانات الكتاب
//    public BookDto updateBook(Long id, BookDto bookDetails) {
//        BookEntity book = bookRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Book not found"));
//
//        book.setTitle(bookDetails.getTitle());
//        book.setAuthor(bookDetails.getAuthor());
//        book.setDescription(bookDetails.getDescription());
//        book.setPublishDate(bookDetails.getPublishDate());
//        book.setVersion(bookDetails.getVersion());
//
//        return bookMapper.toDto(bookRepository.save(book));
//    }
//}
