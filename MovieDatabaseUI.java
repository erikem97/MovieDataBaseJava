package moviedatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
/**
 * A command line user interface for a movie database.
 */
public class MovieDatabaseUI {
	private Scanner _scanner;

	
	/**
	 * Construct a MovieDatabaseUI.
	 */
	public MovieDatabaseUI() {
		
	}
	/**
	 * Start the movie database UI.
	 */
	public void startUI() throws IOException {



		_scanner = new Scanner(System.in);
		int input;		
		boolean quit = false;

		System.out.println("** FILMDATABAS **");

		while(!quit) {			
			input = getNumberInput(_scanner, 1, 4, getMainMenu());

			switch(input) {
			case 1: searchTitel(); break;
			case 2: searchReviewScore(); break;
			case 3: addMovie(); break;
			case 4: quit = true; 			
			}
		}
		//Close scanner to free resources
		_scanner.close();
	}
	/**
	 * Get input and translate it to a valid number.
	 * 
	 * @param scanner the Scanner we use to get input 
	 * @param min the lowest correct number
	 * @param max the highest correct number
	 * @param message message to user
	 * @return the chosen menu number 
	 */
	private int getNumberInput(Scanner scanner, int min, int max, String message) {
		int input = -1;

		while(input < 0) {
			System.out.println(message);
			try {
				input = Integer.parseInt(scanner.nextLine().trim());		
			} 
			catch(NumberFormatException nfe) {
				input = -1;
			}
			if(input < min || input > max) {
				System.out.println("Ogiltigt v�rde.");
			}			
		}			
		return input;
	}
	/**
	 * Get search string from user, search title in the movie 
	 * database and present the search result.
	 */
	private void searchTitel() throws IOException {
		System.out.print("Ange s�kord: ");
		String title = _scanner.nextLine().trim();
		database databaseObject = new database();
		databaseObject.loadMovies();
		databaseObject.searchTitle(title);





		
		
	}
	/**
	 * Get search string from user, search review score in the movie 
	 * database and present the search result.
	 */
	private void searchReviewScore() throws IOException {
		int review = getNumberInput(_scanner, 1, 5, "Ange minimibetyg (1 - 5): ");
		System.out.print("**LADDAR DATABAS**");
		database databaseObject = new database();
		databaseObject.loadMovies();
		databaseObject.searchScore(review);




		
	}	
	/**
	 * Get information from user on the new movie and add
	 * it to the database.
	 */
	private void addMovie() throws IOException {
		System.out.print("Titel: ");
		String title = _scanner.nextLine().trim();
		int reviewScore = getNumberInput(_scanner, 1, 5, "Betyg (1 - 5): ");

		movie movieObject = new movie(title, reviewScore);

		database databaseObject = new database();
		databaseObject.addToDatabase(movieObject);


		
	}


	/**
	 * Return the main menu text.
	 * 
	 * @return the main menu text
	 */
	private String getMainMenu() {
		return  "-------------------\n" +
				"1. S�k p� titel\n" +
				"2. S�k p� betyg\n" +	
				"3. L�gg till film\n" +
				"-------------------\n" + 
				"4. Avsluta";
	}	
}

/**
 * Handels movies with title and score.
 */

class movie{
	String title;
	int score;

	/**
	 * Constructs movie with String title and int score
	 */


	public movie(String title, int score){
		this.title = title;
		this.score = score;
	}

	/**
	 * A selextor method
	 * @return title
	 */

	public String getTitle()
	{
		return title;
	}
	/**
	 * A selextor method
	 * @return score
	 */

	public int getScore()
	{
		return score;
	}


}

/**
 * Used for loading and wirting to the database aswell as doing database searches based on score and title.
 */

class database{
	List<movie> movieArray = new ArrayList<movie>();

	/**
	 * Adds an object of class movie to the database
	 * @param movieObject
	 * @throws IOException
	 */

		public void addToDatabase(movie movieObject) throws IOException {

			Path path = openFile();
			List<String> allLines = Files.readAllLines(path);
			allLines.add("Titel: " + movieObject.getTitle() + " Betyg: " + movieObject.getScore() + "/5");
			writeToFile(path, allLines);

		}

	/**
	 * Opens a file and returns the path
	 * @return path
	 * @throws IOException
	 */

		public Path openFile() throws IOException {
			Path path = Paths.get("text.txt");


			if(!Files.exists(path)) {
				Files.createFile(path);

			}
			return path;
		}

	/**
	 * Adds a specified line to the specified path (database)
	 * @param path
	 * @param allLines
	 * @throws IOException
	 */

		public void writeToFile(Path path, List<String> allLines) throws IOException {
			Files.write(path, allLines);
		}

	/**
	 * Loads all the movies in the database to an array of class movie
	 * @throws IOException
	 */

		public void loadMovies() throws IOException {

			Path path = openFile();
			List<String> allLines = Files.readAllLines(path);

			for(String line : allLines) {
				if(line.length()>10) {
					String movieTitle = line.substring(6, line.length() - 10);
					String movieScoreString = line.substring(line.length()-3,line.length()-2);
					int movieScoreInt = Integer.parseInt(movieScoreString);
					movie movieObject = new movie(movieTitle, movieScoreInt);
					movieArray.add(movieObject);

				}
			}
		}

	/**
	 * Searchers for all movies with a score greater or equal to the specified one and prints them out.
	 * @param score
	 */

	public void searchScore(int score){
			for (moviedatabase.movie movie : movieArray) {
				if (movie.getScore() >= score) {
					printMovie(movie);
				}
			}
		}

	/**
	 * Searchers for all movies with a substring equal to the specified one and prints them out.
	 * @param title
	 */

	public void searchTitle(String title){
		for (moviedatabase.movie movie : movieArray) {
			if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
				printMovie(movie);
			}
		}
	}

	public void printMovie(movie movie){
		System.out.println("Title: " + movie.getTitle() + " Betyg: " + movie.getScore() + "/5");
	}
}

