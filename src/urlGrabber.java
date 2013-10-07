/* 
	Bing Image Grabber - Version 0.1.1

	It generates a JSON object with results from the Bing search, which
	we parse to extract the URLs of images. After that, they are downloaded
	and stored locally.

	Some of the code was used from the GitHub:

	https://github.com/mark-watson/bing_search_java/

	A query URL requires the following components:

		- Root Bing API URL, constant. See "rootURL" in makeQuery();
		- User's API Key, entered in test class
		- Search Term, we use a randomly generated integer
		- Query Type (Web, Images*, Video, etc.)

	The following parameters are optional, but we add them for completeness

		- Query Region, set as en-us for our searches
		- Content Filter, so no adult content... hopefully
		- Number of images per request.

	*In our case the search type is a constant, Images.

	The URL returns a JSON object which we parse to grab URLs of images that are
	returned using the search, The URLs are stored in an String array, and are then
	accessed by the saveImages() method. The size of the array is equal to the
	number of images requested per search term (imageCount parameter in the constructor).
*/
	
import org.apache.commons.codec.binary.Base64;
import com.google.gson.*;
import java.io.*;
import java.net.*;

public class urlGrabber {

	private String encryptedKey; 			// Account Key from Bing search API, passed into constructor
	public String queryTerm;  				// Int generated by generateTerm method
	public String queryFilter;	// Sets default filter to moderate... no obscene images
	public String queryMarket = "en-US";	// Sets default region to US
	public String queryResultAmount;		// Total number of image URLs grabbed from query 
	public String queryRetrunType = "JSON"; // Query will yield a JSON object to parse
	public String queryURL; 				// Final URL sent to Bing, created by makeQuery method
	public String[] storedAddresses; 		// Array of parsed URLs from JSON object
	public String rawQueryTerm;
	
	public urlGrabber(String userAccountKey, String queryChoice, int imageCount, String queryFilter) {
		
		// Found this encryption on GitHub and StackOverflow... Required by MSoft
		byte[] byteKey = Base64.encodeBase64((userAccountKey + ":" + userAccountKey).getBytes());
		this.encryptedKey = new String(byteKey);				// Encrypted Key
		
		if(queryChoice.equals("random"))
			this.queryTerm = generateTerm();					// Search term, 6 digit int
		else this.queryTerm = queryChoice;						// Search term, user input
		
		this.queryFilter = queryFilter;
		this.rawQueryTerm = queryTerm; 							// Used later for directories
		this.queryResultAmount = String.valueOf(imageCount);	// Image download count
		this.storedAddresses = new String[imageCount]; 			// Array containing URLs to download
	}

	// Encompasses all the methods we need to create a folder full of images.
	// Makes the test class super easy to write.
	public void run() throws Exception {

		encodeParameters();					// Formats Parameters for URL
		makeQuery();						// Builds URL
		String jsonLine = runQuery();		// Pulls JSON string from URL
		parseURLs(jsonLine);				// Parses URL from JSON string
		imageDownloader myImageDownloader = new imageDownloader(this);
		myImageDownloader.run();			// Runs through array, downloads images
	}

	// Formats parameters to comply with URL needs
	// Example: 
	public void encodeParameters() {

		this.queryTerm = "?Query=%27" + this.queryTerm + "%27";
		this.queryFilter = "&Adult=%27" + this.queryFilter + "%27";
		this.queryMarket = "&Market=%27" + this.queryMarket + "%27";
		this.queryResultAmount = "&$top=" + this.queryResultAmount; 
		this.queryRetrunType = "&$format=" + this.queryRetrunType;
	}
	
	// Generated the Query to be thrown at Bing. Composed of the root Bing search URL,
	// the search term, the search region, and the content filter. 
	public void makeQuery() {
		
		// StringBuilder sb = new StringBuilder();

		String rootURL = "https://api.datamarket.azure.com/Bing/Search/Image";

		this.queryURL = rootURL + 

					 	  // Necessary field for search
						  this.queryTerm +
						  
						  // Optional fields beyond this point, filling for URL completeness
						  this.queryFilter +
						  this.queryMarket +
						  this.queryResultAmount +

						  // Specifies return Object will be a JSON object
						  this.queryRetrunType;
	} 

	// This method is largely based on Mark Watson's wrapper found at the GitHub repo mentioned
	// above. Put the code in a method for my own organizational structure.
	// Creates URL object based on the String generated from users parameters, and opens an URL 
	// connection to that address. That connections is then passed the authentication we encrypted
	// when this serialGrabber object was created. The resulting JSON string is passed into a string
	// called sb, which is returned to the run method to be used as a parameter for parsing.
	public String runQuery() throws Exception {
		
		URL url = new URL(queryURL);
	    URLConnection urlConnection = url.openConnection();
	    String s1 = "Basic " + encryptedKey;
	    urlConnection.setRequestProperty("Authorization", s1);
	    BufferedReader in = new BufferedReader(new InputStreamReader(
	        urlConnection.getInputStream()));
	    String inputLine;
	    StringBuffer sb = new StringBuffer();
	    while ((inputLine = in.readLine()) != null)
	      sb.append(inputLine);
	    in.close();
	    return sb.toString();
	}

	// Generates a random integer between 100000 and 999999 to be used
	// as the query by the imageGrabber.
	public String generateTerm() {
	
		int generatedNum = 100000 + (int)(Math.random() * ((899999) + 1));
		return String.valueOf(generatedNum);
	}
	
	
	// Parameter is JSON string returned by Bing
	// GSON used as library for parsing
	public void parseURLs(String jsonLine) {
		
		JsonParser jsonParser = new JsonParser();					// New Parser
		JsonArray results = jsonParser.parse(jsonLine)				// Parses JSON into "results" array
			    		.getAsJsonObject().get("d").getAsJsonObject()
			    		.getAsJsonArray("results");
		// Iterator for Array, puts URL in each index
		int i = 0;												
		for(JsonElement result : results) {
			JsonObject resObject = result.getAsJsonObject();	// Turns result element into Object
			String MediaUrl = resObject.get("MediaUrl").getAsString(); // Grabs MediaUrl field from object
			this.storedAddresses[i] = MediaUrl;					// Puts string val of MediaUrl in array
			i++;
		}
	}
}