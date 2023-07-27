package dev.mehmetfd.derivassetdemo.controllers.book_controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The type Create book request dto.
 */
class CreateBookRequestDTO {

  /**
   * The Name.
   */
  @NotBlank
  public String name;
  /**
   * The Year published.
   */
  @NotNull
  public Integer yearPublished;

  /**
   * The Author id.
   */
  @NotNull
  public Long authorId;
}
