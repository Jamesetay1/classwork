package logic;

import java.awt.Color;
import java.util.ArrayList;

import api.Cell;
import api.Flow;

/**
 * Game state for a Flow Free game.
 */
public class FlowGame {
	/**
	 * width is the width of our current flow game, derived from either given
	 * (traditional) or the length of one line of the descriptor (String
	 * descriptor)
	 */
	private int width;
	/**
	 * height is the height of our current flow game, derived from either give
	 * (traditional) or the length of our descriptor list (String descriptor)
	 */
	private int height;
	/**
	 * all Flows is the array of all Flows in our current game This is either
	 * given or done using Util.java.
	 */
	private Flow[] allFlows;
	/**
	 * current Cell is the specific cell we are currently working with
	 * Initialized to null but updated in startFlow, add, and addCell
	 */
	private Cell currentCell;

	/**
	 * Constructs a FlowGame to use the given array of Flows and the given width
	 * and height. Client is responsible for ensuring that all cells in the
	 * given flows have row and column values within the given width and height.
	 * 
	 * @param givenFlows
	 *            an array of Flow objects
	 * @param givenWidth
	 *            width to use for the game
	 * @param givenHeight
	 *            height to use for the game
	 */
	public FlowGame(Flow[] givenFlows, int givenWidth, int givenHeight) {
		allFlows = givenFlows;
		width = givenWidth;
		height = givenHeight;
		currentCell = null;
	}

	/**
	 * Constructs a FlowGame from the given descriptor.
	 * 
	 * @param descriptor
	 *            array of strings representing initial endpoint positions
	 */
	public FlowGame(String[] descriptor) {
		allFlows = Util.createFlowsFromStringArray(descriptor);
		width = descriptor[0].length();
		height = descriptor.length;
		currentCell = null;
	}

	/**
	 * Returns the width for this game.
	 * 
	 * @return width for this game
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height for this game.
	 * 
	 * @return height for this game
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the current cell for this game, possible null.
	 * 
	 * @return current cell for this game
	 */
	public Cell getCurrent() {
		return currentCell;
	}

	/**
	 * Returns all flows for this game. Client should not modify the returned
	 * array or lists.
	 * 
	 * @return array of flows for this game
	 */
	public Flow[] getAllFlows() {
		return allFlows;
	}

	/**
	 * Returns the number of occupied cells in all flows (including endpoints).
	 * 
	 * @return occupied cells in this game
	 */
	public int getCount() {
		int count = 0;
		// goes through all the Flows and gets how many cells are in that flow
		// using .size()
		for (int i = 0; i < allFlows.length; i++) {
			count += allFlows[i].getCells().size();
		}

		return count;
	}

	/**
	 * Returns true if all flows are complete and all cells are occupied.
	 * 
	 * @return true if all flows are complete and all cells are occupied
	 */
	public boolean isComplete() {
		// only is complete if the entire board is full (using height * width)
		return (getCount() == height * width);
	}

	/**
	 * Attempts to set the "current" cell to be an existing cell at the given
	 * row and column. When using a GUI, this method is typically invoked when
	 * the mouse is pressed.
	 * <ul>
	 * <li>Any endpoint can be selected as the current cell. Selecting an
	 * endpoint clears the flow associated with that endpoint.
	 * <li>A non-endpoint cell can be selected as the current cell only if it is
	 * the last cell in a flow.
	 * </ul>
	 * If neither of the above conditions is met, this method does nothing.
	 * 
	 * @param row
	 *            given row
	 * @param col
	 *            given column
	 */
	public void startFlow(int row, int col) {
		ArrayList<Cell> currentCells = new ArrayList<Cell>();

		// iterates through all flows. First finding if its endpoint 1, then
		// endpoint 2,
		// then checking if its the last cell in the list of cells.
		for (int i = 0; i < allFlows.length; i++) {
			if (allFlows[i].getEndpoint(0).positionMatches(row, col)) {
				currentCell = allFlows[i].getEndpoint(0);
				allFlows[i].clear();
				allFlows[i].add(currentCell);

			}
			if (allFlows[i].getEndpoint(1).positionMatches(row, col)) {
				currentCell = allFlows[i].getEndpoint(1);
				allFlows[i].clear();
				allFlows[i].add(currentCell);
			}

			currentCells = allFlows[i].getCells();
			if (currentCells.size() > 1) {
				if (currentCells.get(currentCells.size() - 1).positionMatches(row, col)) {
					currentCell = currentCells.get(currentCells.size() - 1);
				}
			}

		}
	}

	/**
	 * Clears the "current" cell. That is, directly after invoking this method,
	 * <code>getCurrent</code> returns null. When using a GUI, this method is
	 * typically invoked when the mouse is released.
	 */
	public void endFlow() {
		currentCell = null;
	}

	/**
	 * Attempts to add a new cell to the flow containing the current cell. When
	 * using a GUI, this method is typically invoked when the mouse is dragged.
	 * In order to add a cell, the following conditions must be satisfied:
	 * <ol>
	 * <li>The current cell is non-null
	 * <li>The given position is horizontally or vertically adjacent to the
	 * current cell
	 * <li>The given position either is not occupied OR it is occupied by an
	 * endpoint for the flow that is not already in the flow
	 * </ul>
	 * If the three conditions are met, a new cell with the given row/column and
	 * correct color is added to the current flow, and the current cell is
	 * updated to be the new cell.
	 * 
	 * @param row
	 *            given row for the new cell
	 * @param col
	 *            given column for the new cell
	 */
	public void addCell(int row, int col) {
		Flow workingFlow = null;
		Color workingColor = null;
		// This sets a Flow and Color that we are working with in this specific
		// addCell
		// It uses the color of current cell to match up
		for (int i = 0; i < allFlows.length; i++) {
			if (currentCell != null) {
				if (allFlows[i].getColor() == currentCell.getColor()) {
					workingFlow = allFlows[i];
					workingColor = currentCell.getColor();

				}
			}
		}
		// if the cell isn't null, AND is adjacent (but not diagonal) (Helper
		// Method) AND isOccupied AND the the Flow is not complete
		// Then add that cell to the flow.
		if (currentCell != null && isAdjacent(row, col, currentCell) && (!isOccupied(row, col))
				&& !workingFlow.isComplete()) {

			Cell toAdd = new Cell(row, col, workingColor);
			workingFlow.add(toAdd);
			currentCell = new Cell(row, col, workingColor);

			// This is added due to the official clarification, if the one added
			// is either endpoint then set the flow to null (EndFlow())
			for (int i = 0; i < allFlows.length; i++) {
				for (int j = 0; j < 2; j++) {
					if (allFlows[i].getEndpoint(j).positionMatches(row, col)) {
						endFlow();
					}
				}
			}
		}

	}

	/**
	 * Returns true if the row and col position is adjacent (up, left, down or
	 * right ONE block) to the current cell that is is being tested against.
	 * 
	 * @param row
	 *            given row of the coordinates we are wanting to move to
	 * @param col
	 *            give col of the coordinates we are wanting to move to
	 * @param currentCell
	 *            Where we are right now, compare this against row/col
	 * @return true if the given row and col coordinates are adjacent to the
	 *         currentCell, false otherwise.
	 */
	private boolean isAdjacent(int row, int col, Cell currentCell) {
		// If its the same, it is fine.
		if (currentCell.positionMatches(row, col)) return true;
			// If it is only one away in ANY direction (1 up, left down or
			// right) then it is adjacent
			// The absolute distance from where it is to where it is going can
			// only be one (diagonal would be 2)
			if ((Math.abs(currentCell.getRow() - row)) + Math.abs(currentCell.getCol() - col) == 1) {
				return true;
			}

		return false;
	}

	/**
	 * Returns true if the given position is occupied by a cell in a flow in
	 * this game (possibly an endpoint).
	 * 
	 * @param row
	 *            given row
	 * @param col
	 *            given column
	 * @return true if any cell in this game has the given row and column, false
	 *         otherwise
	 */

	public boolean isOccupied(int row, int col) {
		// Check all flow end points and all cells.
		// If it is an end point AND THE SAME COLOR then it will say it is not
		// occupied for
		// addCell() purposes
		for (int i = 0; i < allFlows.length; i++) {
			if (allFlows[i].getEndpoint(0).positionMatches(row, col)
					&& !allFlows[i].getEndpoint(0).colorMatches(currentCell.getColor())) {
				return true;
			}
			if (allFlows[i].getEndpoint(1).positionMatches(row, col)
					&& !allFlows[i].getEndpoint(1).colorMatches(currentCell.getColor())) {
				return true;
			}

			for (int k = 0; k < allFlows[i].getCells().size(); k++) {
				if (allFlows[i].getCells().get(k).positionMatches(row, col)) {
					return true;
				}
			}
		}

		return false;

	}

}