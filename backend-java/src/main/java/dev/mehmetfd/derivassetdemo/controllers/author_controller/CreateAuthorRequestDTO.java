/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 25.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.controllers.author_controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The type Create author request dto.
 */
class CreateAuthorRequestDTO {

  /**
   * The Name.
   */
  @NotBlank
  public String name;
  /**
   * The Year born.
   */
  @NotNull
  public Integer yearBorn;
}
