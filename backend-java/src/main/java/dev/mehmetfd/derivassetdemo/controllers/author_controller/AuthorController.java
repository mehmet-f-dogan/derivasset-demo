/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 24.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.controllers.author_controller;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.projections.AuthorOverviewProjection;
import dev.mehmetfd.derivassetdemo.services.AuthorService;
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
 * The type Author controller.
 */
@RestController
@RequestMapping("/authors")

public class AuthorController {

  private final AuthorService authorService;

  /**
   * Instantiates a new Author controller.
   *
   * @param authorService the author service
   */
  @Autowired
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  /**
   * Gets author by id.
   *
   * @param authorId the author id
   * @return the author by id
   */
  @GetMapping(value = "/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Author> getAuthorById(@PathVariable Long authorId) {
    Optional<Author> authorOptional = authorService.getAuthor(authorId);
    return authorOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Gets author by id cached.
   *
   * @param authorId the author id
   * @return the author by id cached
   */
  @GetMapping(value = "/{authorId}/cached", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Author> getAuthorByIdCached(@PathVariable Long authorId) {
    Optional<Author> authorOptional = authorService.getAuthorCached(authorId);
    return authorOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Gets author overview by id.
   *
   * @param authorId the author id
   * @return the author overview by id
   */
  @GetMapping(value = "/{authorId}/overview", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorOverviewProjection> getAuthorOverviewById(
      @PathVariable Long authorId) {
    Optional<AuthorOverviewProjection> authorOverviewOptional = authorService.getAuthorOverview(
        authorId);
    return authorOverviewOptional.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Delete author by id response entity.
   *
   * @param authorId the author id
   * @return the response entity
   */
  @DeleteMapping("/{authorId}")
  public ResponseEntity deleteAuthorById(@PathVariable Long authorId) {
    boolean result = authorService.deleteAuthorIfExistsById(authorId);
    if (result) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Create author response entity.
   *
   * @param requestObject the request object
   * @return the response entity
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorOverviewProjection> createAuthor(
      @RequestBody @Valid CreateAuthorRequestDTO requestObject) {
    Author author = new Author();
    author.setName(requestObject.name);
    author.setYearBorn(requestObject.yearBorn);
    Optional<Author> savedAuthorOptional = authorService.saveAuthor(author);
    if (savedAuthorOptional.isPresent()) {
      AuthorOverviewProjection authorOverview = authorService.getAuthorOverview(savedAuthorOptional.get().getId()).get();
      return ResponseEntity.ok(authorOverview);
    }
    return ResponseEntity.notFound().build();
  }
}
