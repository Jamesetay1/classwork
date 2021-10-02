package hw4;

import graph.Cell;
/**
 * Extension of Food that moves instead of just flashing.
 * @author James Taylor
 *
 *
 */
public class DungeonessCrab extends Food {

/**
 *  DungeonessCrab's implementation of handle.
 * when the timer resets it moves to an open spot that is close
 * as long as it is not null.
 * 
 * The timer updating happens in super.handle(cell)
 * 
 * @param cell 
 * 		the cell we are working with and from. The cell we are "handling"
 */
	public void handle(Cell cell) {
		super.handle(cell);
		int timer2 = getTime();
		if (timer2 == 0) {
			timer2 = 0;
			
			//when the timer is reset the crab finds a random adjacent open spot
			if (cell.getRandomOpen() != null) {
				cell.moveState(cell.getRandomOpen());
			}
		}
	}

@Override
	public char toChar() {
		return 'D';
	}

}
