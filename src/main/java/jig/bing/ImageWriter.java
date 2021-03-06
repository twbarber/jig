package jig.bing;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Used to store save images to disk
 */
public class ImageWriter {

  private Logger logger = Logger.getLogger(ImageWriter.class);

  ImageWriter() { }

  /**
   * Creates directories in <user_home>/jig/images/<query_term> where images will be
   * saved
   *
   * @return Directory where images where be saved.
   */
  public File makeDirectories() {
    String userHome = System.getProperty("user.home");
    File imageDirectory = new File(userHome + "/grabber/images");
    File queryDirectory = new File(imageDirectory + "/");

    if (!imageDirectory.exists()) {
      if (imageDirectory.mkdir())
        System.out.println("\nBing Directory: " + imageDirectory.toString() + " created");
    }
    if (!queryDirectory.exists()) {
      if (queryDirectory.mkdir())
        System.out.println("\nImageRequest Directory: " + queryDirectory.toString() + " created");
    }
    return queryDirectory;
  }
}
