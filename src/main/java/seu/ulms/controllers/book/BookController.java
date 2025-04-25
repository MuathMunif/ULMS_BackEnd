package seu.ulms.controllers.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.book.BookDto;
import seu.ulms.services.book.BookService;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

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
}
