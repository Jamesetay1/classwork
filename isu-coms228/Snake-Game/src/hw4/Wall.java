package hw4;

import java.awt.Color;

import graph.Cell;
/**
 * the implementation of state for a wall. Including color, the face it is not passable, and its character representation
 * @author James Taylor
 *
 */
public class Wall 
	implements state.State{

	@Override
	public void handle(Cell cell) {	}

	@Override
	public Color getColor() {
		 final Color white = new Color(1f, 1f, 1f);;
		
		return white;
	}

	@Override
	public boolean isPassable() {
		return false;
	}

	@Override
	public char toChar() {
		return '#';
	}

}
