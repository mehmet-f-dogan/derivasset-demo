/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.repositories;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.projections.AuthorOverviewProjection;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Author Repository.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

  /**
   * Gets Author overview by ID
   *
   * @param authorId the {@link Long ID} of the Author
   * @return {@link Optional}{@code <}{@link AuthorOverviewProjection}{@code >} <ul>
   * <li>{@link Optional} containing the {@link AuthorOverviewProjection overview} found</li>
   * <li>{@link Optional#empty()} if an Author cannot be found with the given Author ID</li> </ul>
   * @throws IllegalArgumentException if the provided ID is null
   */
  Optional<AuthorOverviewProjection> getAuthorOverviewById(Long authorId);

  /**
   * Retrieves an Author entity by its ID, along with its associated books fetched eagerly.
   *
   * @param id The {@link Long ID} of the Author to retrieve.
   * @return An {@link Optional} containing the {@link Author} entity with the given ID, and its
   * associated books, if an author with the specified ID exists in the database. An empty
   * {@link Optional} is returned if no author with the given ID is found.
   * @throws IllegalArgumentException if the provided ID is null.
   */
  @EntityGraph(attributePaths = {"books"})
  Optional<Author> findById(Long id);
}