/**
 * <p>Book: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 24.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.controllers.book_controller;

import dev.mehmetfd.derivassetdemo.models.Book;
import dev.mehmetfd.derivassetdemo.models.projections.BookOverviewProjection;
import dev.mehmetfd.derivassetdemo.services.BookService;
import dev.mehmetfd.derivassetdemo.util.RandomDataGenerator;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Book controller.
 */
@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  /**
   * Instantiates a new Book controller.
   *
   * @param bookService the book service
   */
  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  /**
   * Gets book by id.
   *
   * @param bookId the book id
   * @return the book by id
   */
  @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
    Optional<Book> bookOptional = bookService.getBook(bookId);
    return bookOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Gets book by id cached.
   *
   * @param bookId the book id
   * @return the book by id cached
   */
  @GetMapping(value = "/{bookId}/cached", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> getBookByIdCached(@PathVariable Long bookId) {
    Optional<Book> bookOptional = bookService.getCachedBook(bookId);
    return bookOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Gets book overview by id.
   *
   * @param bookId the book id
   * @return the book overview by id
   */
  @GetMapping(value = "/{bookId}/overview", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOverviewProjection> getBookOverviewById(
      @PathVariable Long bookId) {
    Optional<BookOverviewProjection> bookOverviewOptional = bookService.getBookOverview(
        bookId);
    return bookOverviewOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Delete book by id response entity.
   *
   * @param bookId the book id
   * @return the response entity
   */
  @DeleteMapping("/{bookId}")
  public ResponseEntity deleteBookById(@PathVariable Long bookId) {
    boolean result = bookService.deleteBookIfExistsById(bookId);
    if (result) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Create book response entity.
   *
   * @param requestObject the request object
   * @return the response entity
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOverviewProjection> createBook(
      @RequestBody @Valid CreateBookRequestDTO requestObject) {
    Book book = new Book();
    book.setName(requestObject.name);
    book.setYearPublished(requestObject.yearPublished);
    book.setData(RandomDataGenerator.generateBytes(1000000));
    Optional<Book> savedBookOptional = bookService.saveBook(requestObject.authorId, book);
    if (savedBookOptional.isPresent()) {
      BookOverviewProjection bookOverview = bookService.getBookOverview(savedBookOptional.get().getId()).get();
      return ResponseEntity.ok(bookOverview);
    }
    return ResponseEntity.notFound().build();
  }
}
