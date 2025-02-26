package seu.ulms.controllers.book;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // ✅ يسمح فقط لممثل الجامعة بإضافة كتاب جديد
    @PreAuthorize("hasRole('UNIVERSITY_REPRESENTATIVE')")
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid BookDto book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    // ✅ يسمح للأدمن والممثلين برؤية جميع الكتب
    @PreAuthorize("hasAnyRole('ADMIN', 'UNIVERSITY_REPRESENTATIVE')")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // ✅ استرجاع كتاب عبر الـ ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // ✅ يسمح فقط لممثل الجامعة بحذف الكتاب
    @PreAuthorize("hasRole('UNIVERSITY_REPRESENTATIVE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ يسمح فقط لممثل الجامعة بتحديث الكتاب
    @PreAuthorize("hasRole('UNIVERSITY_REPRESENTATIVE')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody @Valid BookDto bookDetails) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDetails));
    }
}
