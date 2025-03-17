package seu.ulms.services.book;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.ulms.dto.book.BookDto;
import seu.ulms.entities.book.BookEntity;
import seu.ulms.mapper.book.BookMapper;
import seu.ulms.repositoies.book.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDto createBook(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepository.save(bookEntity));
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookById(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return bookMapper.toDto(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public BookDto updateBook(Long id, BookDto bookDetails) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setDescription(bookDetails.getDescription());
        book.setPublishDate(bookDetails.getPublishDate());
        book.setVersion(bookDetails.getVersion());
        return bookMapper.toDto(bookRepository.save(book));
    }
}
