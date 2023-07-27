/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.services;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.Book;
import dev.mehmetfd.derivassetdemo.models.projections.BookOverviewProjection;
import dev.mehmetfd.derivassetdemo.repositories.BookRepository;
import dev.mehmetfd.derivassetdemo.services.caching.EntityCachingService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Book Service.
 */
@Service
public class BookService {

  private final BookRepository bookRepository;

  private final AuthorService authorService;
  private EntityCachingService entityCachingService;

  /**
   * Instantiates a new Book Service.
   *
   * @param authorService        the author service
   * @param bookRepository       the {@link BookRepository Book Repository} to be used with the
   *                             service.
   * @param entityCachingService the {@link EntityCachingService Entity Caching Service} to be used
   */
  @Autowired
  public BookService(AuthorService authorService, BookRepository bookRepository, EntityCachingService entityCachingService) {
    this.bookRepository = bookRepository;
    this.authorService = authorService;
    this.entityCachingService = entityCachingService;
  }

  /**
   * Saves Book and adds to Author.
   *
   * @param authorId the Author ID
   * @param book     the {@link Book Book}
   * @return the boolean
   */
  public Optional<Book> saveBook(Long authorId, Book book) {
    if (book == null) {
      return Optional.empty();
    } else if (book.getId() != null) {
      return Optional.empty();
    }
    Optional<Author> foundAuthorOptional = this.authorService.getAuthor(authorId);
    if (foundAuthorOptional.isEmpty()) {
      return Optional.empty();
    }
    book.setAuthor(foundAuthorOptional.get());
    if (this.entityCachingService != null) {
      this.entityCachingService.evictAuthorFromCache(authorId);
    }
    return this.saveBook(book);
  }

  /**
   * Deletes a Book if it exists
   *
   * @param bookId the ID of Book to be deleted
   * @return {@code boolean} <ul>     <li>{@code true} if the deletion is successful.</li>
   * <li>{@code false} if the deletion fails or an Book cannot be found with the provided Book
   * ID.</li> </ul>
   */
  public boolean deleteBookIfExistsById(Long bookId) {
    if (bookId == null) {
      return false;
    }
    if (!bookRepository.existsById(bookId)) {
      return false;
    }
    if (this.entityCachingService != null) {
      this.entityCachingService.evictBookFromCache(bookId);
    }
    this.bookRepository.deleteById(bookId);
    return true;
  }

  /**
   * Gets a Book.
   *
   * @param bookId the ID of Book to be sought
   * @return {@link Optional}{@code <}{@link Book}{@code >} <ul>     <li>{@link Optional} containing
   * the found {@link Book}.</li>     <li>{@link Optional#empty()} if an Book cannot be found with
   * the given Book ID.</li> </ul>
   */
  public Optional<Book> getBook(Long bookId) {
    if (bookId == null) {
      return Optional.empty();
    } else {
      try {
        Optional<Book> foundBookOptional = this.bookRepository.findById(bookId);
        return foundBookOptional;
      } catch (Exception e) {
        return Optional.empty();
      }
    }
  }

  /**
   * Gets an Author, uses Cache.
   *
   * @param bookId the ID of Author to be sought
   * @return {@link Book}
   */
  public Optional<Book> getCachedBook(Long bookId) {
    Book book = this.entityCachingService.getCachedBook(bookId);
    if (book == null) {
      return this.getBook(bookId);
    }
    return Optional.of(book);
  }

  /**
   * Gets Book overview by ID
   *
   * @param bookId the ID of the Book
   * @return {@link Optional}{@code <}{@link BookOverviewProjection}{@code >} <ul>
   * <li>{@link Optional} containing the {@link BookOverviewProjection overview} found</li>
   * <li>{@link Optional#empty()} if an Book cannot be found with the given Book ID</li> </ul>
   */
  public Optional<BookOverviewProjection> getBookOverview(Long bookId) {
    if (bookId == null) {
      return Optional.empty();
    }
    return this.bookRepository.getBookOverviewById(bookId);
  }


  /**
   * Saves a Book.
   *
   * @param book the {@link Book} to be saved
   * @return {@link Optional}{@code <}{@link Book}{@code >} <ul>     <li>{@link Optional} containing
   * the saved {@link Book} if the operation is successful.</li>     <li>{@link Optional#empty()} if
   * the operation fails or the provided Book is null.</li> </ul>
   */
  private Optional<Book> saveBook(@Valid Book book) {
    if (book == null) {
      return Optional.empty();
    }
    try {
      Book savedBook = this.bookRepository.save(book);
      return Optional.of(savedBook);
    } catch (Exception exception) {
      return Optional.empty();
    }
  }
}