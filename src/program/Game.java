package program;
import components.MainOffice;

/**
 * A driver for the game
 * @version 1.0, 9/4/2021
 * @author ItzikRahamim - 312202351
 * @author GilBenHamo - 315744557
 */
public class Game {
	/**
	 * Main program that will run the system
	 * @param args arguments
	 */
	public static void main(String[] args) {
		MainOffice game=new MainOffice(5, 4);
		game.play(60);
		
	}

}
