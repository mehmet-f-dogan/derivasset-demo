/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.Book;
import dev.mehmetfd.derivassetdemo.models.projections.BookOverviewProjection;
import dev.mehmetfd.derivassetdemo.repositories.AuthorRepository;
import dev.mehmetfd.derivassetdemo.repositories.BookRepository;
import dev.mehmetfd.derivassetdemo.util.RandomDataGenerator;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * The type Book Service unit test.
 */
@DataJpaTest
public class BookServiceUnitTest {

  private TestEntityManager entityManager;
  private AuthorService authorService;
  private BookService bookService;

  /**
   * Initialize before each test
   *
   * @param entityManager    the Entity Manager to clear everything after the tests
   * @param authorRepository the Author Repository
   * @param bookRepository   the Book Repository
   */
  @BeforeEach
  public void initialize(@Autowired TestEntityManager entityManager,
      @Autowired AuthorRepository authorRepository, @Autowired BookRepository bookRepository) {
    this.authorService = new AuthorService(authorRepository, null);
    this.bookService = new BookService(authorService, bookRepository, null);
    this.entityManager = entityManager;
    this.entityManager.clear();
  }

  /**
   * Remove every object from entity tree
   */
  @AfterEach
  public void teardown() {
    this.entityManager.clear();
  }

  private Author generateAndSaveValidAuthor() {
    Author author = new Author();
    author.setName("Author");
    author.setYearBorn(1000);
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);
    assertTrue(savedAuthorOptional.isPresent());
    return savedAuthorOptional.get();
  }

  private Book generateValidBook() {
    Book book = new Book();
    book.setName("Book");
    book.setYearPublished(2023);
    Author author = generateAndSaveValidAuthor();
    book.setAuthor(author);
    book.setData(RandomDataGenerator.generateBytes(16));
    return book;
  }

  /**
   * Test save invalid book.
   */
  @Test
  public void testSaveInvalidBook() {
    // Given
    Book book = new Book();
    book.setAuthor(null);

    // When
    Optional<Book> savedBookOptional = bookService.saveBook(-1L, book);

    // Then
    assertTrue(savedBookOptional.isEmpty());

    // When
    savedBookOptional = bookService.saveBook(1L, null);

    // Then
    assertTrue(savedBookOptional.isEmpty());
  }

  /**
   * Test save valid book
   */
  @Test
  public void testSaveValidBook() {
    // Given
    Book book = generateValidBook();

    // When
    Optional<Book> savedBookOptional = bookService.saveBook(book.getAuthor().getId(), book);

    // Then
    assertTrue(savedBookOptional.isPresent());

    Book savedBook = savedBookOptional.get();
    assertNotNull(savedBook.getId());
    assertEquals(book.getName(), savedBook.getName());
    assertEquals(book.getYearPublished(), savedBook.getYearPublished());

    Author savedBookAuthor = savedBook.getAuthor();
    assertNotNull(savedBookAuthor);
    assertEquals(book.getAuthor(), savedBookAuthor);
  }

  /**
   * Test delete book with invalid id
   */
  @Test
  public void testDeleteBookWithInvalidId() {
    // Given
    Long bookId = (long) -10;

    // When
    boolean deletionResult = bookService.deleteBookIfExistsById(bookId);

    // Then
    assertFalse(deletionResult);

    // When
    deletionResult = bookService.deleteBookIfExistsById(null);

    // Then
    assertFalse(deletionResult);
  }

  /**
   * Test delete book with valid id
   */
  @Test
  public void testDeleteBookWithValidId() {
    // Given
    Book book = generateValidBook();
    Optional<Book> savedBookOptional = bookService.saveBook(book.getAuthor().getId(), book);
    assumeTrue(savedBookOptional.isPresent());
    Book savedBook = savedBookOptional.get();

    // When
    boolean deletionResult = bookService.deleteBookIfExistsById(savedBook.getId());

    // Then
    assertTrue(deletionResult);
  }

  /**
   * Test find book by invalid book id
   */
  @Test
  public void testFindBookByInvalidBookId() {
    // Given
    Book book = generateValidBook();
    Optional<Book> savedBookOptional = bookService.saveBook(book.getAuthor().getId(), book);
    assumeTrue(savedBookOptional.isPresent());
    Book savedBook = savedBookOptional.get();

    // When
    Optional<Book> foundBookOptional = bookService.getBook(savedBook.getId() + 1);

    // Then
    assertTrue(foundBookOptional.isEmpty());

    //When
    foundBookOptional = bookService.getBook(null);

    // Then
    assertTrue(foundBookOptional.isEmpty());
  }

  /**
   * Test find book by valid book id
   */
  @Test
  public void testFindBookByValidBookId() {
    // Given
    Book book = generateValidBook();
    Optional<Book> savedBookOptional = bookService.saveBook(book.getAuthor().getId(), book);
    assumeTrue(savedBookOptional.isPresent());
    Book savedBook = savedBookOptional.get();

    // When
    Optional<Book> foundBookOptional = bookService.getBook(savedBook.getId());

    // Then
    assertTrue(foundBookOptional.isPresent());
  }

  /**
   * Test find invalid book overview
   */
  @Test
  public void testFindInvalidBookOverview() {
    // Given
    Book book = generateValidBook();
    Optional<Book> savedBookOptional = bookService.saveBook(book.getAuthor().getId(), book);
    assumeTrue(savedBookOptional.isPresent());
    Book savedBook = savedBookOptional.get();

    // When
    Optional<BookOverviewProjection> foundBookOverviewOptional = bookService.getBookOverview(
        savedBook.getId() + 1);

    // Then
    assertTrue(foundBookOverviewOptional.isEmpty());

    // When
    foundBookOverviewOptional = bookService.getBookOverview(null);

    // Then
    assertTrue(foundBookOverviewOptional.isEmpty());
  }

  /**
   * Test find valid book overview
   */
  @Test
  public void testFindValidBookOverview() {
    // Given
    Book book = generateValidBook();
    Optional<Book> savedBookOptional = bookService.saveBook(book.getAuthor().getId(), book);
    assumeTrue(savedBookOptional.isPresent());
    Book savedBook = savedBookOptional.get();

    // When
    Optional<BookOverviewProjection> foundBookOverviewOptional = bookService.getBookOverview(
        savedBook.getId());

    // Then
    assertTrue(foundBookOverviewOptional.isPresent());

    BookOverviewProjection bookOverview = foundBookOverviewOptional.get();
    assertEquals(bookOverview.getName(), savedBook.getName());
    assertEquals(bookOverview.getYearPublished(), savedBook.getYearPublished());
  }

  /**
   * Test register with invalid author id
   */
  @Test
  public void testRegisterWithInvalidAuthorID() {
    Optional<Book> result = this.bookService.saveBook(-1L, null);
    assertTrue(result.isEmpty());
    result = this.bookService.saveBook(null, null);
    assertTrue(result.isEmpty());
  }

  /**
   * Test register with invalid book
   */
  @Test
  public void testRegisterWithInvalidBook() {
    Author existingAuthor = generateAndSaveValidAuthor();
    Book book = generateValidBook();
    book.setName(null);
    Optional<Book> result = this.bookService.saveBook(existingAuthor.getId(), book);
    assertTrue(result.isEmpty());
    result = this.bookService.saveBook(existingAuthor.getId(), null);
    assertTrue(result.isEmpty());
  }

  /**
   * Test register book with existing author to new author
   */
  @Test
  public void testRegisterBookWithExistingAuthorToNewAuthor() {
    Book book = generateValidBook();
    Author existingAuthor = generateAndSaveValidAuthor();
    book.setAuthor(existingAuthor);
    bookService.saveBook(book.getAuthor().getId(), book);
    Author newAuthor = generateAndSaveValidAuthor();
    Optional<Book> result = this.bookService.saveBook(newAuthor.getId(), book);
    assertTrue(result.isEmpty());
  }

  /**
   * Test re-register book with same author
   */
  @Test
  public void testReRegisterBookWithSameAuthor() {
    Book book = generateValidBook();
    Author existingAuthor = generateAndSaveValidAuthor();
    book.setAuthor(existingAuthor);
    bookService.saveBook(book.getAuthor().getId(), book);
    Optional<Book> result = this.bookService.saveBook(existingAuthor.getId(), book);
    assertTrue(result.isEmpty());
  }

  /**
   * Test successful book registration
   */
  @Test
  public void testSuccessfulBookRegistration() {
    Book book = generateValidBook();
    Author author = generateAndSaveValidAuthor();
    book.setAuthor(author);
    Optional<Book> result = this.bookService.saveBook(author.getId(), book);
    assertTrue(result.isPresent());
  }
}
