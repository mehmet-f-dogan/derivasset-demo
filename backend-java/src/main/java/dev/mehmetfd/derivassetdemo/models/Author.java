/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.mehmetfd.derivassetdemo.models.projections.AuthorOverviewProjection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Fetch;

/**
 * The type Author.
 */
@Entity
public class Author implements Serializable {
  @Serial
  @Transient
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank
  @Column(nullable = false)
  private String name;
  @NotNull
  @Column(nullable = false)
  private Integer yearBorn;
  @NotNull
  @Column(nullable = false)
  @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
  private List<Book> books = new ArrayList<Book>();

  /**
   * Instantiates a new Author.
   */
  public Author() {
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return this.id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(@NotNull Long id) {
    this.id = id;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(@NotBlank String name) {
    this.name = name;
  }

  /**
   * Gets year born.
   *
   * @return the year born
   */
  public Integer getYearBorn() {
    return this.yearBorn;
  }

  /**
   * Sets year born.
   *
   * @param yearBorn the year born
   */
  public void setYearBorn(@NotNull Integer yearBorn) {
    this.yearBorn = yearBorn;
  }

  /**
   * Gets authors books.
   *
   * @return the books
   */
  public List<Book> getBooks() {
    return this.books;
  }

  /**
   * Sets books.
   *
   * @param books the books
   */
  public void setBooks(@NotNull List<Book> books) {
    this.books = books;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof final Author other)) {
      return false;
    }
    if (!other.canEqual(this)) {
      return false;
    }
    final Object this$id = this.getId();
    final Object other$id = other.getId();
    if (!Objects.equals(this$id, other$id)) {
      return false;
    }
    final Object this$name = this.getName();
    final Object other$name = other.getName();
    if (!Objects.equals(this$name, other$name)) {
      return false;
    }
    final Object this$yearOfBirth = this.getYearBorn();
    final Object other$yearOfBirth = other.getYearBorn();
    if (!Objects.equals(this$yearOfBirth, other$yearOfBirth)) {
      return false;
    }
    final Object this$books = this.getBooks();
    final Object other$books = other.getBooks();
    return Objects.equals(this$books, other$books);
  }

  /**
   * Can equal boolean.
   *
   * @param other the other
   * @return the boolean
   */
  protected boolean canEqual(final Object other) {
    return other instanceof Author;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final Object $yearOfBirth = this.getYearBorn();
    result = result * PRIME + ($yearOfBirth == null ? 43 : $yearOfBirth.hashCode());
    final Object $books = this.getBooks();
    result = result * PRIME + ($books == null ? 43 : $books.hashCode());
    return result;
  }

  public String toString() {
    return "Author(id=" + this.getId() + ", name=" + this.getName() + ", yearOfBirth="
        + this.getYearBorn() + ")";
  }
}
