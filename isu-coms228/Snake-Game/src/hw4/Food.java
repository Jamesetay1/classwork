package hw4;

import java.awt.Color;
import graph.Cell;
import main.Config;

/**
 * creates a food that the snake can eat to grow in length
 * includes its flashing, representation, color, and whether or not it can be passed through.
 * @author James Taylor
 *
 */
public class Food 
	implements state.State{

	private int timer; 

	@Override
	public void handle(Cell cell) {
		timer++;
		if (timer >= Config.MAX_FOOD_TIMER){
			timer = 0;	
		}	
	}
	/**
	 * protected accessor method so that DungeonessCrab can get the time
	 * @return the variable timer
	 */
	protected int getTime(){
		return timer;
	}

	@Override
	public Color getColor() {
		return Config.FOOD_COLORS[timer];
	}


	@Override
	public boolean isPassable() {
		return true;
	}

	@Override
	public char toChar() {
		return 'F';
	}

}
