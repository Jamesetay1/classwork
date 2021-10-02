package mini2;

import java.util.ArrayList;

import api.ITransform;

/**
 * Utility class for applying transformations to 2d arrays. A transformation
 * computes a new value for a cell that is based on the square neighborhood of
 * cells that surround it. Each transformation is an implementation of the
 * ITransform interface.
 */
public class GridUtil {
	/**
	 * Applies the given transformation to all cells of the given array and
	 * produces a new array of the same size. The original array is not
	 * modified.
	 * 
	 * @param arr
	 *            given array
	 * @param transform
	 *            transformation to apply
	 * @return new array consisting of transformed values
	 */
	public static int[][] applyAll(int[][] arr, ITransform transform) {
		// THIS METHOD IS FULLY IMPLEMENTED AND SHOULD NOT BE MODIFIED
		int numRows = arr.length;
		int numCols = arr[0].length;
		int[][] result = new int[numRows][numCols];
		for (int row = 0; row < numRows; row += 1) {
			for (int col = 0; col < numCols; col += 1) {
				int[][] subArray = getSubArray(arr, row, col,
						transform.getRadius(), transform.isWrapped());
				int newValue = transform.apply(subArray);
				result[row][col] = newValue;
			}
		}
		return result;
	}

	/**
	 * Displays the contents of a 2d int array
	 * 
	 * @param arr
	 *            a 2d array of ints
	 */
	public static void printArray(int[][] arr) {
		for (int row = 0; row < arr.length; row += 1) {
			for (int col = 0; col < arr[0].length; col += 1) {
				System.out.printf("%3d", arr[row][col]);
			}
			System.out.println();
		}
	}

	/**
	 * Returns the neighborhood surrounding the specified cell. In general, the
	 * returned array is a square sub-array of <code>arr</code>, with width and
	 * height 2 * <code>radius</code> + 1, whose center is
	 * <code>arr[centerRow][centerCol]</code>. For cells within
	 * <code>radius</code> of the edge, the value for out-of-range indices is
	 * determined by the <code>wrapped</code> parameter, as follows:
	 * <ul>
	 * <li>if <code>wrapped</code> is false, cells for out-of-range indices are
	 * filled with zeros
	 * <li>if <code>wrapped</code> is true, cells for out-of-range indices are
	 * determined by "wrapping" the indices:
	 * <ul>
	 * <li>for a row index, the array height is added to or subtracted from the
	 * index to bring it within range
	 * <li>for a column index, the array width is added to or subtracted from
	 * the index to bring it within range
	 * </ul>
	 * </ul>
	 * Note that the size of neighborhood, neighborhood size 2 *
	 * <code>radius</code> + 1, is not allowed to be greater than either the
	 * width or height of the given array.
	 * 
	 * @param arr
	 *            the original array
	 * @param centerRow
	 *            row index of center cell
	 * @param centerCol
	 *            column index of center cell
	 * @param radius
	 *            radius of the neighborhood (return array has width and height
	 *            equal to 2 * <code>radius</code> + 1
	 * @param wrapped
	 *            true if out-of-range indices should be wrapped, false if cells
	 *            should be filled with zeros
	 * @return a new 2d array consisting of the cells surrounding the given
	 *         center cell
	 * @throws IllegalArgumentException
	 *             if the neighborhood size 2 * <code>radius</code> + 1 is
	 *             greater than the given array's width or height
	 */
	public static int[][] getSubArray(int[][] arr, int centerRow, int centerCol,
			int radius, boolean wrapped) {

		if ((2 * radius) + 1 > arr.length || (2 * radius) + 1 > arr[0].length) {
			throw new IllegalArgumentException();
		}

		int startRow = centerRow - radius;
		int startCol = centerCol - radius;
		int endRow = centerRow + radius;
		int endCol = centerCol + radius;

		int[][] subA = new int[2 * radius + 1][2 * radius + 1];
		ArrayList<Integer> toAdd = new ArrayList<Integer>();
		int z = 0;

		//Condition if wrapped that goes through and adds to AList based on opposite position if need be.
		if (wrapped == true) {
			for (int i = startRow; i <= endRow; i++) {
				for (int j = startCol; j <= endCol; j++) {
					// Top Left
					if (i < 0 && j < 0) {
						toAdd.add(arr[arr.length + i][arr[0].length + j]);
					}
					// Top Right
					else if (i < 0 && j > arr[0].length - 1) {
						toAdd.add(arr[arr.length + i][j - arr[0].length]);
					}
					// Bottom Right
					else if (i > arr.length - 1 && j > arr[0].length - 1) {
						toAdd.add(arr[i - arr.length][j - arr[0].length]);
					}
					// Bottom Left
					else if (i > arr.length - 1 && j < 0) {
						toAdd.add(arr[i - arr.length][arr[0].length + j]);
					}
					// Strictly Left
					else if (j < 0) {
						toAdd.add(arr[i][arr[0].length + j]);
					}
					// Strictly Top
					else if (i < 0) {
						toAdd.add(arr[(arr.length + i)][j]);
					}
					// Strictly Right
					else if (j > arr[0].length - 1) {
						toAdd.add(arr[i][j - arr[0].length]);
					}
					// Strictly Bottom
					else if (i > arr.length - 1) {
						toAdd.add(arr[i - arr.length][j]);
					} else {
						toAdd.add(arr[i][j]);
						// System.out.println(arr[i][j] + ", ");
					}
				}
			}
		}
		// Just adds the number UNLESS its out of bounds, then its a 0.
		else if (wrapped == false) {
			for (int i = startRow; i <= endRow; i++) {
				for (int j = startCol; j <= endCol; j++) {
					if (i < 0)toAdd.add(0);
					else if (j < 0)toAdd.add(0);  
					else if (i > arr.length - 1)toAdd.add(0);
					else if(j > arr[0].length - 1)toAdd.add(0); 
					else {
					toAdd.add(arr[i][j]);
					}
				}
			}
		}
		
		//Fills the new array with the numbers stored in AList
		for (int x = 0; x < (2 * radius) + 1; x++) {
			for (int y = 0; y < (2 * radius) + 1; y++) {
				subA[x][y] = toAdd.get(z);
				z++;
			}
		}

		return subA;
	}
}
