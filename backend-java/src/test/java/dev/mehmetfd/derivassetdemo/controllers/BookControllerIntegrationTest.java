/**
 * <p>Book: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 24.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.controllers;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.Book;
import dev.mehmetfd.derivassetdemo.repositories.AuthorRepository;
import dev.mehmetfd.derivassetdemo.repositories.BookRepository;
import dev.mehmetfd.derivassetdemo.util.RandomDataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * The type Book controller integration test.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integration-tests.properties")
public class BookControllerIntegrationTest {

  private WebTestClient webTestClient;

  private BookRepository bookRepository;
  private AuthorRepository authorRepository;

  /**
   * Initialize before each.
   *
   * @param webTestClient    the web test client
   * @param bookRepository   the book repository
   * @param authorRepository the author repository
   */
  @BeforeEach
  public void initialize(@Autowired WebTestClient webTestClient,
      @Autowired BookRepository bookRepository,
      @Autowired AuthorRepository authorRepository) {
    this.webTestClient = webTestClient;
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
    this.bookRepository.deleteAll();
    this.authorRepository.deleteAll();
  }

  /**
   * Remove every Book
   */
  @AfterEach
  public void teardown() {
    this.webTestClient.delete();
    this.bookRepository.deleteAll();
    this.authorRepository.deleteAll();
  }


  private Book generateAndSaveValidBook() {
    Author author = new Author();
    author.setYearBorn(1000);
    author.setName("Author");

    this.authorRepository.save(author);

    Book book = new Book();
    book.setName("Book");
    book.setYearPublished(2000);
    book.setAuthor(author);
    book.setData(RandomDataGenerator.generateBytes(1));

    this.bookRepository.save(book);

    return book;
  }

  /**
   * Test get by invalid id.
   */
  @Test
  public void testGetByInvalidId() {
    this.webTestClient.get()
        .uri("/books/-1")
        .exchange()
        .expectStatus()
        .isNotFound();
    this.webTestClient.get()
        .uri("/books/bad-id")
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  /**
   * Test get by valid id.
   */
  @Test
  @DirtiesContext
  public void testGetByValidId() {
    Book book = generateAndSaveValidBook();
    this.webTestClient.get()
        .uri("/books/" + book.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json("{\"id\":1, \"name\": " + book.getName() + ", \"yearPublished\": " + book.getYearPublished()
            + "}");
  }

  /**
   * Test get overview by invalid id.
   */
  @Test
  public void testGetOverviewByInvalidId() {
    this.webTestClient.get()
        .uri("/books/-1/overview")
        .exchange()
        .expectStatus()
        .isNotFound();
    this.webTestClient.get()
        .uri("/books/bad-id/overview")
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  /**
   * Test delete by invalid id.
   */
  @Test
  public void testDeleteByInvalidId() {
    this.webTestClient.delete()
        .uri("/books/-1")
        .exchange()
        .expectStatus()
        .isNotFound();
    this.webTestClient.delete()
        .uri("/books/bad-id")
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  /**
   * Test delete by valid id.
   */
  @Test
  @DirtiesContext
  public void testDeleteByValidId() {
    generateAndSaveValidBook();
    this.webTestClient.delete()
        .uri("/books/1")
        .exchange()
        .expectStatus()
        .isOk();
  }

  /**
   * Test create book with invalid body.
   */
  @Test
  public void testCreateBookWithInvalidBody() {
    this.webTestClient.post()
        .uri("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"name\": \"Book\""
            + "}"))
        .exchange()
        .expectStatus()
        .isBadRequest();
    this.webTestClient.post()
        .uri("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"yearPublished\": 2000"
            + "}"))
        .exchange()
        .expectStatus()
        .isBadRequest();
    this.webTestClient.post()
        .uri("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"name\": null"
            + "\"yearPublished\": null"
            + "}"))
        .exchange()
        .expectStatus()
        .isBadRequest();
    this.webTestClient.post()
        .uri("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  /**
   * Test create book with valid body.
   */
  @Test
  @DirtiesContext
  public void testCreateBookWithValidBody() {
    Author author = new Author();
    author.setYearBorn(1000);
    author.setName("Author");
    this.authorRepository.save(author);

    this.webTestClient.post()
        .uri("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"name\": \"Book\","
            + "\"yearPublished\": 2000,"
            + "\"authorId\": 1"
            + "}"))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json("{\"id\":1, \"name\": \"Book\", \"yearPublished\": 2000}");
  }
}
