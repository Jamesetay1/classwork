package hw4;

import java.awt.Color;

import graph.Cell;
import main.Config;
import state.SnakeSegment;
/**
 * The snakes head including the snakes length, movement through cells, when it
 * eats food, its color and representation.
 * 
 * @author James Taylor
 *
 */
public class SnakeHead implements state.State, state.Snake {
	/**
	 * length of the snake, starts at 4.
	 */
	private int length = 4;
	/**
	 * the snakes timer
	 */
	private int timer = 0;

	@Override
	public int getLength() {

		return length;
	}

	@Override
	public void handle(Cell cell) {

		Cell closer = cell.getRandomCloser();
		Cell open = cell.getRandomOpen();

		// when the time runs out, tries to get a position closer to the mouse
		if (timer == Config.MAX_SNAKE_TIMER - 1) {
			timer = 0;
			if (closer != null) {
				// if such a position is food, add to length
				if (closer.getState() instanceof Food) {
					length++;
				}
				//moves the snake head to where we are headed and replaces the head with a segment
				cell.moveState(closer);
				cell.setState(new SnakeSegment(this));
			//otherwise when the time runs out, tries to get any open random adjacent position
			} else if (open != null) {
				// if such a position is food, add to length
				if (open.getState() instanceof Food) {
					length++;
				}
				//moves the snake head to where we are headed and replaces the head with a segment
				cell.moveState(open);
				cell.setState(new SnakeSegment(this));
			} else {
				//if it can't move anywhere adjacent then the game ends
				Config.endGame(getLength());
			}
		}
		timer++;
	}

	@Override
	public Color getColor() {
		final Color cyan = new Color(.0f, .9f, .9f);
		return cyan;
	}

	@Override
	public boolean isPassable() {
		return false;
	}

	@Override
	public char toChar() {
		return 'S';
	}

}
