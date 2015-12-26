package jig.bing;

import jig.constants.AdultOption;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImageRequestFactoryTest {

  ImageRequestFactory testRequestFactory = new ImageRequestFactory();

	@Test
	public void testTerm() {
    String expected = "https://api.datamarket.azure.com/Bing/Search/Image?$format=JSON" +
        "&Query=%27Test%27&Adult=%27Strict%27&$top=50";
    ImageRequest testRequest = testRequestFactory.createRequest("Test");
    assertEquals(expected, testRequest.getRequestUrlAsString());
	}

  @Test
  public void testNumber() {
    ImageRequest testRequest = testRequestFactory.createRequest(100);
    assertTrue(testRequest.getRequestUrlAsString().contains("top=100"));
  }

  @Test
  public void testAdult() {
    ImageRequest testRequest = testRequestFactory.createRequest(AdultOption.STRICT);
    assertTrue(testRequest.getRequestUrlAsString().contains("%27Strict%27"));
  }

  @Test
  public void testTermAndAdult() {
    ImageRequest testRequest = testRequestFactory.createRequest("Test", AdultOption.STRICT);
    assertTrue(testRequest.getRequestUrlAsString().contains("%27Test%27"));
    assertTrue(testRequest.getRequestUrlAsString().contains("%27Strict%27"));
  }

  @Test
  public void testTermAndNumber() {
    String expected = "https://api.datamarket.azure.com/Bing/Search/Image?$format=JSON" +
        "&Query=%27Test%27&Adult=%27Strict%27&$top=50";
    ImageRequest testRequest = testRequestFactory.createRequest("Test", 50);
    assertEquals(expected, testRequest.getRequestUrlAsString());
  }

  @Test
  public void testAdultAndNumber() {
    ImageRequest testRequest = testRequestFactory.createRequest(50, AdultOption.STRICT);
    assertTrue(testRequest.getRequestUrlAsString().contains("top=50"));
    assertTrue(testRequest.getRequestUrlAsString().contains("%27Strict%27"));  }

  @Test
  public void testTermNumberAndAdult() {
    ImageRequest testRequest = testRequestFactory.createRequest("Test", 50, AdultOption.STRICT);
    assertTrue(testRequest.getRequestUrlAsString().contains("%27Test%27"));
    assertTrue(testRequest.getRequestUrlAsString().contains("top=50"));
    assertTrue(testRequest.getRequestUrlAsString().contains("%27Strict%27"));
  }

  @Test
  public void testQueryWithSpaces() {
    String expected = "https://api.datamarket.azure.com/Bing/Search/Image?$format=JSON" +
        "&Query=%27Test+One%27&Adult=%27Strict%27&$top=50";
    ImageRequest testRequest = testRequestFactory.createRequest("Test One", 50, AdultOption.STRICT);
    assertEquals(expected, testRequest.getRequestUrlAsString());
  }

}


