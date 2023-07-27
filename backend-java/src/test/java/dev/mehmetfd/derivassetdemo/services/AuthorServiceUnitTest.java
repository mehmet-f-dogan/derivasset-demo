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
import dev.mehmetfd.derivassetdemo.models.projections.AuthorOverviewProjection;
import dev.mehmetfd.derivassetdemo.repositories.AuthorRepository;
import dev.mehmetfd.derivassetdemo.repositories.BookRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * The type Author Service unit test.
 */
@DataJpaTest
public class AuthorServiceUnitTest {

  private TestEntityManager entityManager;

  private AuthorService authorService;

  /**
   * Initialize before each test
   *
   * @param entityManager    the Entity Manager to clear everything after the tests
   * @param authorRepository the Author Repository
   */
  @BeforeEach

  public void initialize(@Autowired TestEntityManager entityManager,
      @Autowired AuthorRepository authorRepository) {
    this.authorService = new AuthorService(authorRepository, null);
    this.entityManager = entityManager;
    this.entityManager.clear();
  }

  /**
   * Remove every object from entity tree after each test
   */
  @AfterEach
  public void teardown() {
    this.entityManager.clear();
  }

  private Author generateValidAuthor() {
    Author author = new Author();
    author.setName("Author");
    author.setYearBorn(1000);
    return author;
  }

  /**
   * Test save invalid author
   */
  @Test
  public void testSaveInvalidAuthor() {
    // Given
    Author author = new Author();

    // When
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);

    // Then
    assertTrue(savedAuthorOptional.isEmpty());

    // When
    savedAuthorOptional = authorService.saveAuthor(null);

    // Then
    assertTrue(savedAuthorOptional.isEmpty());
  }

  /**
   * Test save valid author
   */
  @Test
  public void testSaveValidAuthor() {
    // Given
    Author author = generateValidAuthor();

    // When
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);

    // Then
    assertTrue(savedAuthorOptional.isPresent());

    Author savedAuthor = savedAuthorOptional.get();
    assertNotNull(savedAuthor.getId());
    assertEquals(author.getName(), savedAuthor.getName());
    assertEquals(author.getYearBorn(), savedAuthor.getYearBorn());

    List<Book> savedAuthorBooks = savedAuthor.getBooks();
    assertNotNull(savedAuthorBooks);
    assertEquals(author.getBooks().size(), savedAuthorBooks.size());
  }

  /**
   * Test delete author with invalid id
   */
  @Test
  public void testDeleteAuthorWithInvalidId() {
    // Given
    Long authorId = (long) -10;

    // When
    boolean deletionResult = authorService.deleteAuthorIfExistsById(authorId);

    // Then
    assertFalse(deletionResult);

    // When
    deletionResult = authorService.deleteAuthorIfExistsById(null);

    // Then
    assertFalse(deletionResult);
  }

  /**
   * Test delete author with valid id
   */
  @Test
  public void testDeleteAuthorWithValidId() {
    // Given
    Author author = generateValidAuthor();
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);
    assumeTrue(savedAuthorOptional.isPresent());
    Author savedAuthor = savedAuthorOptional.get();

    // When
    boolean deletionResult = authorService.deleteAuthorIfExistsById(savedAuthor.getId());

    // Then
    assertTrue(deletionResult);
  }

  /**
   * Test get author by invalid author id
   */
  @Test
  public void testGetAuthorByInvalidAuthorId() {
    // Given
    Author author = generateValidAuthor();
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);
    assumeTrue(savedAuthorOptional.isPresent());
    Author savedAuthor = savedAuthorOptional.get();

    // When
    Optional<Author> foundAuthorOptional = authorService.getAuthor(savedAuthor.getId() + 1);

    // Then
    assertTrue(foundAuthorOptional.isEmpty());

    //When
    foundAuthorOptional = authorService.getAuthor(null);

    // Then
    assertTrue(foundAuthorOptional.isEmpty());
  }

  /**
   * Test get author by valid author id
   */
  @Test
  public void testGetAuthorByValidAuthorId() {
    // Given
    Author author = generateValidAuthor();
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);
    assumeTrue(savedAuthorOptional.isPresent());
    Author savedAuthor = savedAuthorOptional.get();

    // When
    Optional<Author> foundAuthorOptional = authorService.getAuthor(savedAuthor.getId());

    // Then
    assertTrue(foundAuthorOptional.isPresent());
  }

  /**
   * Test get invalid author overview
   */
  @Test
  public void testGetInvalidAuthorOverview() {
    // Given
    Author author = generateValidAuthor();
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);
    assumeTrue(savedAuthorOptional.isPresent());
    Author savedAuthor = savedAuthorOptional.get();

    // When
    Optional<AuthorOverviewProjection> foundAuthorOverviewOptional = authorService.getAuthorOverview(
        savedAuthor.getId() + 1);

    // Then
    assertTrue(foundAuthorOverviewOptional.isEmpty());

    // When
    foundAuthorOverviewOptional = authorService.getAuthorOverview(null);

    // Then
    assertTrue(foundAuthorOverviewOptional.isEmpty());
  }

  /**
   * Test get valid author overview
   */
  @Test
  public void testGetValidAuthorOverview() {
    // Given
    Author author = generateValidAuthor();
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);
    assumeTrue(savedAuthorOptional.isPresent());
    Author savedAuthor = savedAuthorOptional.get();

    // When
    Optional<AuthorOverviewProjection> foundAuthorOverviewOptional = authorService.getAuthorOverview(
        savedAuthor.getId());

    // Then
    assertTrue(foundAuthorOverviewOptional.isPresent());

    AuthorOverviewProjection authorOverview = foundAuthorOverviewOptional.get();
    assertEquals(authorOverview.getName(), savedAuthor.getName());
    assertEquals(authorOverview.getYearBorn(), savedAuthor.getYearBorn());
  }
}
