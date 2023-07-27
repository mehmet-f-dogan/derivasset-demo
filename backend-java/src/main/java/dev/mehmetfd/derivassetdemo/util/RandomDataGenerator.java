/**
 * <p>Author: Mehmet F. Dogan <a href="mailto:mehmet@mehmetfd.dev">mehmet@mehmetfd.dev</a></p>
 * <p>Created: 23.07.2023</p>
 */
package dev.mehmetfd.derivassetdemo.util;

import java.util.Random;

/**
 * The type Random data generator.
 */
public class RandomDataGenerator {

  private static final Random random = new Random();

  /**
   * Generate random bytes.
   *
   * @param max_length the max length
   * @return the byte [ ]
   */
  public static byte[] generateBytes(int max_length) {
    int generatedBytesLength = random.nextInt(max_length) + 1;
    byte[] resultBytes = new byte[generatedBytesLength];
    random.nextBytes(resultBytes);
    return resultBytes;
  }
}