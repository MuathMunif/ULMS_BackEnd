package seu.ulms.controllers.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seu.ulms.dto.book.BookDto;
import seu.ulms.dto.book.PublicBookDto;
import seu.ulms.services.book.AttachmentService;
import seu.ulms.services.book.BookService;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AttachmentService attachmentService;

    //  يسمح فقط لممثل الجامعة بإضافة كتاب جديد
    @PreAuthorize("hasAnyRole('UNIVERSITY_REPRESENTATIVE','ADMIN')")
    @PostMapping
    public ResponseEntity<BookDto> createOrUpdate(@RequestBody @Valid BookDto book) {
        return ResponseEntity.ok(bookService.createOrUpdateBook(book));
    }

    //  جلب جميع الكتب مع دعم `Pagination`
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BookDto>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/public_book")
    public ResponseEntity<Page<PublicBookDto>> getAllBooksToPublic(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooksToPublic(pageable));
    }

    //  جلب جميع الكتب الخاصة بجامعة معينة
    @PreAuthorize("hasAnyRole('UNIVERSITY_REPRESENTATIVE','ADMIN','STUDENT','ROLE_STUDENT')")
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<BookDto>> getBooksByUniversity(@PathVariable Long universityId) {
        return ResponseEntity.ok(bookService.getBooksByUniversity(universityId));
    }

    //  البحث عن كتاب عبر العنوان
    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    //  استرجاع كتاب عبر الـ ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    //  يسمح فقط لممثل الجامعة بحذف الكتاب
    @PreAuthorize("hasAnyRole('UNIVERSITY_REPRESENTATIVE','ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    //  يسمح فقط لممثل الجامعة بتحديث الكتاب
    @PreAuthorize("hasAnyRole('UNIVERSITY_REPRESENTATIVE','ADMIN')")
    @PutMapping("edit/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto bookDetails) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDetails));
    }


    @PostMapping("/upload_with_data")
    public ResponseEntity<BookDto> uploadBookWithFile(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("publishDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDate,
            @RequestParam("version") String version,
            @RequestParam("universityId") Long universityId,
            @RequestParam("categoryName") String categoryName,
            @RequestPart("file") MultipartFile file) {

        // 1. ارفع الملف
        var attachment = attachmentService.uploadAttachment(file);

        // 2. جهز الكتاب
        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setDescription(description);
        bookDto.setAuthor(author);
        bookDto.setPublishDate(publishDate);
        bookDto.setVersion(version);
        bookDto.setUniversityId(universityId);
        bookDto.setCategoryName(categoryName);
        bookDto.setAttachmentId(attachment.getId());

        // 3. احفظ الكتاب
        BookDto savedBook = bookService.createOrUpdateBook(bookDto);

        return ResponseEntity.ok(savedBook);
    }



    // لعرض الكتب للطلاب المخول لهم
    @PreAuthorize("hasRole('STUDENT')") // فقط الطلاب
    @GetMapping("/student/{universityId}")
    public ResponseEntity<List<BookDto>> viewApprovedUniversityBooks(@PathVariable Long universityId) {
        List<BookDto> books = bookService.getBooksForStudentByUniversity(universityId);
        return ResponseEntity.ok(books);
    }
}
