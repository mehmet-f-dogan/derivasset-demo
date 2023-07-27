/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.mehmetfd.derivassetdemo.models.projections.BookOverviewProjection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * The type Book.
 */
@NamedEntityGraph(name = "Book.overview", attributeNodes = {
    @NamedAttributeNode("id"),
    @NamedAttributeNode("name"),
    @NamedAttributeNode("yearPublished")
})
@Entity
public class Book implements Serializable {

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
  private Integer yearPublished;
  @JsonIgnore
  @NotNull
  @JoinColumn(name = "author_id", nullable = false)
  @ManyToOne
  private Author author;
  @NotNull
  @Column(nullable = false)
  private byte[] data;

  /**
   * Instantiates a new Book.
   */
  public Book() {
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
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public @NotBlank String getName() {
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
   * Gets year published.
   *
   * @return the year published
   */
  public @NotNull Integer getYearPublished() {
    return this.yearPublished;
  }

  /**
   * Sets year published.
   *
   * @param yearPublished the year published
   */
  public void setYearPublished(@NotNull Integer yearPublished) {
    this.yearPublished = yearPublished;
  }

  /**
   * Gets author.
   *
   * @return the author
   */
  public @NotNull Author getAuthor() {
    return this.author;
  }

  /**
   * Sets author.
   *
   * @param author the author
   */
  public void setAuthor(@NotNull Author author) {
    this.author = author;
  }

  /**
   * Gets data.
   *
   * @return the data
   */
  public @NotNull byte[] getData() {
    return this.data;
  }

  /**
   * Sets data.
   *
   * @param data the data
   */
  public void setData(@NotNull byte[] data) {
    this.data = data;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof final Book other)) {
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
    final Object this$yearOfPublication = this.getYearPublished();
    final Object other$yearOfPublication = other.getYearPublished();
    if (!Objects.equals(this$yearOfPublication, other$yearOfPublication)) {
      return false;
    }
    final Object this$author = this.getAuthor();
    final Object other$author = other.getAuthor();
    if (!Objects.equals(this$author, other$author)) {
      return false;
    }
    final Object this$data = this.getData();
    final Object other$data = other.getData();
    return Objects.equals(this$data, other$data);
  }

  /**
   * Can equal boolean.
   *
   * @param other the other
   * @return the boolean
   */
  protected boolean canEqual(final Object other) {
    return other instanceof Book;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final Object $yearOfPublication = this.getYearPublished();
    result = result * PRIME + ($yearOfPublication == null ? 43 : $yearOfPublication.hashCode());
    final Object $author = this.getAuthor();
    result = result * PRIME + ($author == null ? 43 : $author.hashCode());
    final Object $data = this.getData();
    result = result * PRIME + ($data == null ? 43 : $data.hashCode());
    return result;
  }

  public String toString() {
    return "Book(id=" + this.getId() + ", name=" + this.getName() + ", yearOfPublication="
        + this.getYearPublished() + ")";
  }
}