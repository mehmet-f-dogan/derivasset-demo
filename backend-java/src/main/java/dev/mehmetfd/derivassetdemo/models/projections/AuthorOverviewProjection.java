/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.models.projections;

import dev.mehmetfd.derivassetdemo.models.Author;
import dev.mehmetfd.derivassetdemo.models.Book;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonComponent;

/**
 * The interface for the Author overviews.
 */
@JsonComponent
public interface AuthorOverviewProjection {

  /**
   * Gets the Author ID
   *
   * @return the ID of the {@link Author Author}
   */
  Long getId();

  /**
   * Gets name of the Author
   *
   * @return the name of the {@link Author Author}
   */
  String getName();

  /**
   * Gets the year Author was born
   *
   * @return the year of birth of {@link Author Author}
   */
  Integer getYearBorn();
}