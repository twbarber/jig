package com.big;
public class QueryBuilder {
	
	private String queryTerm;  				// Int generated by generateTerm method
	private String queryFilter;				// Sets default filter to moderate... no obscene images
	private String queryMarket = "en-US";	// Sets default region to US
	private String queryResultAmount;		// Total number of image URLs grabbed from query 
	private String queryRetrunType = "JSON"; // Query will yield a JSON object to parse
	private String queryOffset;				// Used if Result amount > 50
	private String[] queryURL; 				// Final URL sent to Bing, created by makeQuery method
	private int remainingImages;
	
	public QueryBuilder(String queryChoice, int imageCount, String queryFilter) {

		if(queryChoice.equals("random"))
			this.queryTerm = generateTerm();					// Search term, 6 digit int
		else this.queryTerm = queryChoice;						// Search term, user input
		this.queryFilter = queryFilter;
		if(imageCount > 50) {
			this.queryResultAmount = "50";
			this.remainingImages = imageCount - 50;
		}
		else this.queryResultAmount = String.valueOf(imageCount);	// Image download count
		this.queryOffset = String.valueOf(0);
	}
	
	// Generates a random integer between 100000 and 999999 to be used
	// as the query by the imageGrabber.
	public String generateTerm() {

		int generatedNum = 100000 + (int)(Math.random() * ((899999) + 1));
		return String.valueOf(generatedNum);
	}
	
	// Formats parameters to comply with URL needs
	// Example: 
	public void encodeParameters() {

		this.queryTerm = "?Query=%27" + this.queryTerm + "%27";
		this.queryFilter = "&Adult=%27" + this.queryFilter + "%27";
		this.queryMarket = "&Market=%27" + this.queryMarket + "%27";
		this.queryResultAmount = "&$top=" + this.queryResultAmount; 
		this.queryOffset = "&$skip=" + this.queryOffset;
		this.queryRetrunType = "&$format=" + this.queryRetrunType;
	}

	// Generated the Query to be thrown at Bing. Composed of the root Bing search URL,
	// the search term, the search region, and the content filter. 
	public void makeQuery() {

		String rootURL = "https://api.datamarket.azure.com/Bing/Search/Image";
		
		for(int i = 0; i < this.remainingImages; i++) {
			
			this.queryURL[i] = rootURL + 

					// Necessary field for search
					this.queryTerm +

					// Optional fields beyond this point, filling for URL completeness
					this.queryFilter +
					this.queryMarket +
					this.queryResultAmount +
					this.queryOffset +

					// Specifies return Object will be a JSON object
					this.queryRetrunType;
					
			adjustRemainingImageCount();
		}
	} 
	
	public void adjustRemainingImageCount() {
		
		if(this.remainingImages > 50) {
			this.remainingImages -= 50;
		}
		else {
			this.queryResultAmount = String.valueOf(this.remainingImages);
			this.remainingImages = 0;
		}
	}
}
