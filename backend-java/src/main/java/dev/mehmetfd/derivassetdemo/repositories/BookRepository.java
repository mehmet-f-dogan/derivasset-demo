/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.repositories;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.Book;
import dev.mehmetfd.derivassetdemo.models.projections.BookOverviewProjection;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Book Repository.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  /**
   * Gets Book overview by ID
   *
   * @param bookId the {@link Long ID} of the Book
   * @return {@link Optional}{@code <}{@link BookOverviewProjection}{@code >} <ul>
   * <li>{@link Optional} containing the {@link BookOverviewProjection overview} found</li>
   * <li>{@link Optional#empty()} if an Book cannot be found with the given Book ID</li> </ul>
   * @throws IllegalArgumentException if the provided ID is null
   */
  Optional<BookOverviewProjection> getBookOverviewById(Long bookId);

  /**
   * Retrieves a Book entity by its ID, along with its associated Author fetched eagerly.
   *
   * @param id The {@link Long ID} of the Author to retrieve.
   * @return An {@link Optional} containing the {@link Book} entity with the given ID, and its
   * associated books, if an author with the specified ID exists in the database. An empty
   * {@link Optional} is returned if no author with the given ID is found.
   * @throws IllegalArgumentException if the provided ID is null.
   */
  //@EntityGraph(attributePaths = {"author"})
  //Optional<Book> findById(Long id);
}