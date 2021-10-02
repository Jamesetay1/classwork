package hw4;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import graph.Cell;
import graph.GraphMap;
/**
 * makes the map based on the information given (2D Array and width and height of pixels
 * @author James Taylor
 *
 */
public class SquareMap 
	extends GraphMap{

	@Override
	public int getPixelWidth() {
		return ((getCells()[0].length*getDistance())+(getDistance()));

	}

	@Override
	public int getPixelHeight() {
		return ((getCells().length*getDistance())+(getDistance()));

	}

	@Override
	public Cell[] createNeighbors(int col, int row) {
		ArrayList<Cell> making = new ArrayList<Cell>();
		Cell[][] grid = getCells();
		
		//goes through the whole array and picks out the adjacent cells that are not out of bounds, adds them to array list
		for (int i = 0; i<getCells().length; i++){
			for (int j = 0; j<getCells()[0].length; j++){
				 if ((i == row && j == col-1) && (!(row<0 || row > getCells().length || col<0 || col>getCells()[0].length))){
					 making.add(grid[i][j]);
				 }
				 else if ((i == row && j == col+1) && (!(row<0 || row > getCells().length || col<0 || col>getCells()[0].length))){
					 making.add(grid[i][j]);
				 }
				 else if ((i == row+1 && j == col) && (!(row<0 || row > getCells().length || col<0 || col>getCells()[0].length))){
					 making.add(grid[i][j]);
				 }
				 else if ((i == row-1 && j == col) && (!(row<0 || row > getCells().length || col<0 || col>getCells()[0].length))){
					 making.add(grid[i][j]);
				 }
			}
		}
		Cell[] array = making.toArray(new Cell[making.size()]);
		return array;
	}

	@Override
	protected Point selectClosestIndex(int x, int y) {
		int row = 0; int col = 0;
		row = (y-(getDistance()/2))/getDistance();
		col = (x-(getDistance()/2))/getDistance();
		return (new Point(col, row));
	}

	@Override
	public Polygon createPolygon(int col, int row) {
		int d2 = getDistance()/2;
		//puts the corners based on position and pixel distance then then makes a new Polygon that the program will draw
		int[] xpoints = {d2+(col*getDistance()), d2+((col+1)*getDistance()), d2+((col+1)*getDistance()),d2+(col*getDistance())};
		int[] ypoints = {d2+(row*getDistance()), d2+(row*getDistance()), d2+((row+1)*getDistance()), d2+((row+1)*getDistance())};
				Polygon making = new Polygon(xpoints, ypoints, 4);
		
		return making;
	}

}
