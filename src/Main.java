/* 
	Bing Image Grabber - Version 0.2.1

	Main class. Gets BingID from user and makes objects.

	TODO: 
		- Make iterations to run multiple querys
 */

import java.io.File;
import java.util.Scanner;

public class Main {

	public static Scanner in = new Scanner(System.in);

	// Sets default values of content filter to Moderate, and images to 30
	// If custom search is chosen, user enters search term, and chooses other parameters
	public static void main(String[] args) throws Exception {

		String adult = "Moderate";
		int numImages = -1;
		boolean goodNumber = false;
		
		System.out.println("\nBing Image Grabber 0.2.0\n");

		firstRun();

		String queryChoice = queryMenu();
		if(queryChoice.equalsIgnoreCase("random"))
			numImages = 30;
		else{
			adult = filterMenu();
			while(!goodNumber) {
				System.out.print("\nDesired Number of Images (Max 50: ");
				numImages = in.nextInt();
				if(numImages < 0 || numImages > 1000)
					System.out.println("\nPlease enter a valid number of images...");
				else goodNumber = true;
			}
		}
		urlGrabber mySerialGrabber = new urlGrabber(queryChoice, numImages, adult);
		mySerialGrabber.run();	
	}

	public static void firstRun() {



		String userHome = System.getProperty("user.home");
		String strDirectory = userHome + "/big";

		File bigDir = new File(strDirectory);

		if (!bigDir.exists()) {
			boolean success = bigDir.mkdir();
			if(success)	
				System.out.println("BIG Directory Initialized\n");
		}
	}

	public static String queryMenu() {

		boolean validChoice = false;

		System.out.println("Choose Search Term: \n" +
				"\n1) Random \n2) Custom");  

		while(!validChoice) {
			System.out.print("\nChoice: ");

			String choice = in.nextLine();

			if(choice.equals("1") || choice.equalsIgnoreCase("random"))
				return "random";
			else if(choice.equals("2") || choice.equalsIgnoreCase("custom")) {
				System.out.print("\nEnter Search Term: ");
				return in.nextLine().replaceAll(" ", "_");
			}	
			else 
				System.out.println("\nInvalid Option");
		}
		return "random";	// Returns random as default if somehow it gets here
	}

	// Displays menu of available content filters.
	// User chooses by entering number, loops until good input
	public static String filterMenu() {

		boolean validChoice = false;

		System.out.println("\nChoose Adult Content Settings: \n" +
				"\n1) Strict \n2) Moderate \n3) Off");

		while(!validChoice) {
			System.out.print("\nChoice: ");

			String choice = in.nextLine();

			if(choice.equals("1") || choice.equalsIgnoreCase("strict"))
				return "Strict";
			else if(choice.equals("2") || choice.equalsIgnoreCase("moderate")) {
				return "Moderate";
			}	
			else if(choice.equals("3") || choice.equalsIgnoreCase("off")) {
				return "Off";
			}	
			else 
				System.out.println("\nInvalid Option");
		}
		return "Moderate";	// Returns Moderate as default if somehow it gets here
	}
}