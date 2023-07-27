/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.services;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.projections.AuthorOverviewProjection;
import dev.mehmetfd.derivassetdemo.repositories.AuthorRepository;
import dev.mehmetfd.derivassetdemo.repositories.BookRepository;
import dev.mehmetfd.derivassetdemo.services.caching.EntityCachingService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * The type Author Service.
 */
@Service
public class AuthorService {


  private final AuthorRepository authorRepository;

  private final EntityCachingService entityCachingService;

  /**
   * Instantiates a new Author Service.
   *
   * @param authorRepository     the {@link AuthorRepository Author Repository} to be used with the
   *                             service.
   * @param entityCachingService the {@link EntityCachingService Entity Caching Service} to be used
   */
  @Autowired
  public AuthorService(AuthorRepository authorRepository, EntityCachingService entityCachingService) {
    this.authorRepository = authorRepository;
    this.entityCachingService = entityCachingService;
  }

  /**
   * Saves an Author.
   *
   * @param author the {@link Author} to be saved
   * @return {@link Optional}{@code <}{@link Author}{@code >} <ul>     <li>{@link Optional}
   * containing the saved {@link Author} if the operation is successful.</li>
   * <li>{@link Optional#empty()} if the operation fails or the provided Author is null.</li> </ul>
   */
  public Optional<Author> saveAuthor(Author author) {
    if (author == null) {
      return Optional.empty();
    }
    try {
      Author savedAuthor = this.authorRepository.save(author);
      return Optional.of(savedAuthor);
    } catch (Exception exception) {
      return Optional.empty();
    }
  }

  /**
   * Deletes an Author if it exists
   *
   * @param authorId the ID of Author to be deleted
   * @return {@code boolean} <ul>     <li>{@code true} if the deletion is successful.</li>
   * <li>{@code false} if the deletion fails or an Author cannot be found with the provided Author
   * ID.</li> </ul>
   */
  public boolean deleteAuthorIfExistsById(Long authorId) {
    if (authorId == null) {
      return false;
    }
    if (!authorRepository.existsById(authorId)) {
      return false;
    }
    if (this.entityCachingService != null) {
      this.entityCachingService.evictAuthorFromCache(authorId);
    }
    this.authorRepository.deleteById(authorId);
    return true;
  }

  /**
   * Gets an Author.
   *
   * @param authorId the ID of Author to be sought
   * @return {@link Optional}{@code <}{@link Author}{@code >} <ul>     <li>{@link Optional}
   * containing the found {@link Author}.</li>     <li>{@link Optional#empty()} if an Author cannot
   * be found with the given Author ID.</li> </ul>
   */
  public Optional<Author> getAuthor(Long authorId) {
    if (authorId == null) {
      return Optional.empty();
    } else {
      Optional<Author> authorOptional = this.authorRepository.findById(authorId);
      return authorOptional;
    }
  }

  /**
   * Gets an Author, uses Cache.
   *
   * @param authorId the ID of Author to be sought
   * @return {@link Author}
   */
  public Optional<Author> getAuthorCached(Long authorId) {
    Author author = this.entityCachingService.getCachedAuthor(authorId);
    if (author == null) {
      return Optional.empty();
    }
    return Optional.of(author);
  }


  /**
   * Gets Author overview by ID
   *
   * @param authorId the ID of the Author
   * @return {@link Optional}{@code <}{@link AuthorOverviewProjection}{@code >} <ul>
   * <li>{@link Optional} containing the {@link AuthorOverviewProjection overview} found</li>
   * <li>{@link Optional#empty()} if an Author cannot be found with the given Author ID</li> </ul>
   */
  public Optional<AuthorOverviewProjection> getAuthorOverview(Long authorId) {
    if (authorId == null) {
      return Optional.empty();
    }
    return this.authorRepository.getAuthorOverviewById(authorId);
  }
}