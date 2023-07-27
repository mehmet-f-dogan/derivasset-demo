/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 24.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.controllers;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.repositories.AuthorRepository;
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
import org.springframework.web.reactive.function.BodyInserters;

/**
 * The type Author controller integration test.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integration-tests.properties")
public class AuthorControllerIntegrationTest {

  private WebTestClient webTestClient;

  private AuthorRepository authorRepository;

  /**
   * Initialize before each.
   *
   * @param webTestClient    the web test client
   * @param authorRepository the author repository
   */
  @BeforeEach
  public void initialize(@Autowired WebTestClient webTestClient,
      @Autowired AuthorRepository authorRepository) {
    this.webTestClient = webTestClient;
    this.authorRepository = authorRepository;
    this.authorRepository.deleteAll();
  }

  /**
   * Remove every Author
   */
  @AfterEach
  public void teardown() {
    this.webTestClient.delete();
    this.authorRepository.deleteAll();
  }

  private Author generateAndSaveValidAuthor() {
    Author author = new Author();
    author.setName("Author");
    author.setYearBorn(1000);
    return this.authorRepository.save(author);
  }

  /**
   * Test get by invalid id.
   */
  @Test
  public void testGetByInvalidId() {
    this.webTestClient.get()
        .uri("/authors/-1")
        .exchange()
        .expectStatus()
        .isNotFound();
    this.webTestClient.get()
        .uri("/authors/bad-id")
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
    Author author = generateAndSaveValidAuthor();
    this.webTestClient.get()
        .uri("/authors/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json("{\"id\":1, \"name\": " + author.getName() + ", \"yearBorn\": " + author.getYearBorn()
            + "}");
  }

  /**
   * Test get overview by invalid id.
   */
  @Test
  public void testGetOverviewByInvalidId() {
    this.webTestClient.get()
        .uri("/authors/-1/overview")
        .exchange()
        .expectStatus()
        .isNotFound();
    this.webTestClient.get()
        .uri("/authors/bad-id/overview")
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
        .uri("/authors/-1")
        .exchange()
        .expectStatus()
        .isNotFound();
    this.webTestClient.delete()
        .uri("/authors/bad-id")
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
    generateAndSaveValidAuthor();
    this.webTestClient.delete()
        .uri("/authors/1")
        .exchange()
        .expectStatus()
        .isOk();
  }

  /**
   * Test create author with invalid body.
   */
  @Test
  public void testCreateAuthorWithInvalidBody() {
    this.webTestClient.post()
        .uri("/authors")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"name\": \"Author\""
            + "}"))
        .exchange()
        .expectStatus()
        .isBadRequest();
    this.webTestClient.post()
        .uri("/authors")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"yearBorn\": 1000"
            + "}"))
        .exchange()
        .expectStatus()
        .isBadRequest();
    this.webTestClient.post()
        .uri("/authors")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"name\": null"
            + "\"yearBorn\": null"
            + "}"))
        .exchange()
        .expectStatus()
        .isBadRequest();
    this.webTestClient.post()
        .uri("/authors")
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  /**
   * Test create author with valid body.
   */
  @Test
  @DirtiesContext
  public void testCreateAuthorWithValidBody() {
    this.webTestClient.post()
        .uri("/authors")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue("{"
            + "\"name\": \"Author\","
            + "\"yearBorn\": 1000"
            + "}"))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json("{\"id\":1, \"name\": \"Author\", \"yearBorn\": 1000}");
  }
}
