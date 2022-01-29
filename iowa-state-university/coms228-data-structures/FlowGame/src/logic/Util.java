package logic;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

import api.Cell;
import api.Flow;

/**
 * Utility class with methods for creating Flows from string descriptors and
 * reading string descriptors from a file. A string descriptor is an array of
 * strings, all with the same length, in which an alphabetic character at a
 * given row and column represents a cell at that row and column. The color of
 * the cell is determined from the character by the Cell constructor. A
 * descriptor is invalid if not all strings are the same length or if there is
 * an alphabetic character that does not appear exactly twice.
 */

public class Util {

	/**
	 * Creates an array of Flow objects based on the string descriptor. If the
	 * descriptor is invalid, this method returns null.
	 * 
	 * @param descriptor
	 *            array of strings
	 * @return array of Flow objects determined by the descriptor
	 */
	public static Flow[] createFlowsFromStringArray(String[] descriptor) {

		Color currentColor;
		int colorCount = 0;
		Cell currentCell, otherCell;
		ArrayList<Cell> myCells = new ArrayList<Cell>();
		ArrayList<Flow> myList = new ArrayList<Flow>();
		
		//goes through and compares all the lengths of the lines, if they aren't the same
		//its an invalid descriptor and it returns null.
		for (int k = 0; k < descriptor.length - 1; k++) {
			if (descriptor[k].length() != descriptor[k + 1].length())
				return null;
		}

		// double for loop that looks through every line and says if it is not a '-'
		//then make it a cell.
		for (int i = 0; i < descriptor.length; i++) {
			for (int j = 0; j < descriptor[i].length(); j++) {
				if (descriptor[i].charAt(j) != '-') {
					myCells.add(new Cell(i, j, descriptor[i].charAt(j)));
				}
			}
		}
		//begins to go through all the cells and takes the first one.
		//then it begins again at the next cell and looks for its match
		//it returns null if their is more than 2 or only 1.
		for (int l = 0; l < myCells.size(); l++) {
			currentColor = myCells.get(l).getColor();
			currentCell = myCells.get(l);
			colorCount = 0;

			for (int m = l + 1; m < myCells.size(); m++) {
				if (currentColor == myCells.get(m).getColor()) {
					otherCell = myCells.get(m);
					colorCount++;
					myList.add(new Flow(currentCell, otherCell));
					if (colorCount > 1)
						return null;
				}
			}
			//if it didn't find another one then its null.
			if (l == myCells.size() && colorCount == 0)
				return null;
			colorCount = 0;
		}

		return myList.toArray(new Flow[0]);
	}

	/**
	 * Reads the given file and constructs a list of FlowGame objects, one for
	 * each descriptor in the file. Descriptors in the file are separated by
	 * some amount of whitespace, but the file need not end with whitespace and
	 * may have extra whitespace at the beginning. Invalid descriptors in the
	 * file are ignored, so the method may return an empty list.
	 * 
	 * @param filename
	 *            name of the file to read
	 * @return list of FlowGame objects created from the valid descriptors in
	 *         the file
	 * @throws FileNotFoundException
	 */

	public static ArrayList<FlowGame> readFile(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner scan = new Scanner(file);

		ArrayList<String> myInputStringList = new ArrayList<String>();
		ArrayList<FlowGame> myFlowGames = new ArrayList<FlowGame>();
		int width = 0;
		int height = 0;
		String line = "";
		
		//while there is another line to look at it executes all of these
		//first it trims and sets a line.
		while (scan.hasNextLine()) {
			line = scan.nextLine().trim();
			//if the line isn't empty then it adds the line to a list, and updates the height and width.
			if (!line.isEmpty()) {

				myInputStringList.add(line);
				height = myInputStringList.size();
				width = line.length();
			}
			//if the line is empty OR there is not another line
			if (line.isEmpty() || !scan.hasNextLine()) {

				// Changes my arraylist to a list so that
				// createFlowsFromStringArray can accept it
				String[] myInputStringArray = myInputStringList.toArray(new String[myInputStringList.size()]);

				//gets an array back from createFlowsFromStringArray and adds the objects into a list
				//"myFlowGames"
				Flow[] myInputFlowArray = createFlowsFromStringArray(myInputStringArray);
				myFlowGames.add(new FlowGame(myInputFlowArray, width, height));

				// RESET EVERYTHING!
				height = 0;
				width = 0;
				myInputStringList.clear();
				myInputStringArray = null;
				myInputFlowArray = null;
			}

		}
		scan.close();

		return myFlowGames;

	}
}
