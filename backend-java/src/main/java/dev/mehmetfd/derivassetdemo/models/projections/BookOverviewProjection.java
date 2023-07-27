/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.models.projections;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonComponent;

/**
 * The interface for the Book overviews.
 */
@JsonComponent
public interface BookOverviewProjection {

  /**
   * Gets the Book ID
   *
   * @return the ID of the {@link Book Book}
   */
  Long getId();

  /**
   * Gets name of the Book
   *
   * @return the name of the {@link Book Book}
   */
  String getName();

  /**
   * Gets the year Book was published
   *
   * @return the year of publishing of {@link Book Book}
   */
  Integer getYearPublished();

}