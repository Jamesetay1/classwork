package edu.iastate.cs228;

/**
 *  
 * @author James Taylor
 *
 */

import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * This abstract class is extended by SelectionSort, InsertionSort, MergeSort, and QuickSort.
 * It stores the input (later on the sorted) sequence and records the employed sorting algorithm, 
 * the comparison method, and the time spent on sorting. 
 *
 */


public abstract class AbstractSorter
{
	
	protected Point[] points;    // Array of points operated on by a sorting algorithm. 
	                             // The number of points is given by points.length.
	
	protected String algorithm = null; // "selection sort", "insertion sort",  
	                                   // "merge sort", or "quick sort". Initialized by a subclass 
									   // constructor.
	protected boolean sortByAngle;     // true if last sort was done by polar angle and false 
									   // if by x-coordinate 
	
	protected String outputFileName;   // "select.txt", "insert.txt", "merge.txt", or "quick.txt"
	
	protected long sortingTime; 	   // execution time in nanoseconds. 
	 
	protected Comparator<Point> pointComparator;  // comparator which compares polar angle if 
												  // sortByAngle == true and x-coordinate if 
												  // sortByAngle == false 
	
	private Point lowestPoint; 	    // lowest point in the array, or in case of a tie, the
									// leftmost of the lowest points. This point is used 
									// as the reference point for polar angle based comparison.

	
	// Add other protected or private instance variables you may need. 
	
	protected AbstractSorter()
	{
		// No implementation needed. Provides a default super constructor to subclasses. 
		// Removable after implementing SelectionSorter, InsertionSorter, MergeSorter, and QuickSorter.
	}
	
	
	/**
	 * This constructor accepts an array of points as input. Copy the points into the array points[]. 
	 * Sets the instance variable lowestPoint.
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	protected AbstractSorter(Point[] pts) throws IllegalArgumentException
	{
		if (pts == null)throw new IllegalArgumentException("");
		if (pts.length == 0)throw new IllegalArgumentException("");
		
		points = pts.clone();
		
		
		//Now Get and set lowest point
		lowestPoint = points[0];
		for(int i = 1; i<points.length; i++){
			if(points[i].getY() < lowestPoint.getY()){  //if the Y of this point is lower than the one we are holding
			    lowestPoint = points[i];//set the point as the lowest
			}
			if(points[i].getY() == lowestPoint.getY() && points[i].getX() < lowestPoint.getX()){
				 lowestPoint = points[i];
			}
			
		}
	
	}
	
	/**
	 * This constructor reads points from a file. Sets the instance variables lowestPoint and 
	 * outputFileName.
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	protected AbstractSorter(String inputFileName) throws FileNotFoundException, InputMismatchException
	{	
		File file = new File(inputFileName);
		Scanner counter = new Scanner(file);
		String temp = "";
		int count = 0;

		//scanning file, making a new string to take pts from later while also counting
		while (counter.hasNextInt()) {
			temp += (counter.nextInt()) + " ";
			count++;
		}
		counter.close();
		
		if (count%2==1)throw new InputMismatchException();//If number of pts is odd, throw exception
		
		//scanning that temp file to get our new pts
		Scanner scan = new Scanner(temp);
		points = new Point[count / 2];

		int i = 0;
		while(scan.hasNextInt()) {
			points[i] = new Point(scan.nextInt(), scan.nextInt());
			i++;
		}
		
			scan.close();

	//Get and Set Lowest Point	
		lowestPoint = points[0];
		for(int j = 0; j<points.length; j++){
			if(points[j].getY() < lowestPoint.getY()){  //if the Y of this point is lower than the one we are holding
			    lowestPoint = points[j];//set the point as the lowest
			}
			if(points[j].getY() == lowestPoint.getY() && points[j].getX() < lowestPoint.getX()){
				 lowestPoint = points[j];
			}
			
		}
		
		 
	}
	

	/**
	 * Sorts the elements in points[]. 
	 * 
	 *     a) in the non-decreasing order of x-coordinate if order == 1
	 *     b) in the non-decreasing order of polar angle w.r.t. lowestPoint if order == 2 
	 *        (lowestPoint will be at index 0 after sorting)
	 * 
	 * Sets the instance variable sortByAngle based on the value of order. Calls the method 
	 * setComparator() to set the variable pointComparator and use it in sorting.    
	 * Records the sorting time (in nanoseconds) using the System.nanoTime() method. 
	 * (Assign the time to the variable sortingTime.)  
	 * 
	 * @param order  1   by x-coordinate 
	 * 			     2   by polar angle w.r.t lowestPoint 
	 *
	 * @throws IllegalArgumentException if order is less than 1 or greater than 2
	 */
	public abstract void sort(int order) throws IllegalArgumentException; 
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the assignment description. 
	 */
	public String stats()
	{
		//prints out the needed stat, fills the rest of the allotted space with spaces (taken from spec)
		String temp = algorithm;
		int length = 20 - algorithm.length();
		for (int i = 0; i < length; i++) {
			temp += " ";
		}
		temp += (points.length);
		length = 12 - Integer.toString(points.length).length();
		for (int i = 0; i < length; i++) {
			temp += " ";
		}
		temp += Long.toString(sortingTime);
		return temp;

		
	}
	
	
	/**
	 * Write points[] to a string.  When printed, the points will appear in order of increasing
	 * index with every point occupying a separate line.  The x and y coordinates of the point are 
	 * displayed on the same line with exactly one blank space in between. 
	 */
	@Override
	public String toString()
	{
		String temp = "";
		
		for (int i = 0; i < points.length; i++){
			temp+= (points[i].getX() + " " + points[i].getY()+"\n");
		}
		return temp;
	}

	
	/**
	 *  
	 * This method, called after sorting, writes point data into a file by outputFileName. It will 
	 * be used for Mathematica plotting to verify the sorting result.  The data format depends on 
	 * sortByAngle.  It is detailed in Section 4.1 of the assignment description assn2.pdf. 
	 * @throws IOException 
	 */
	public void writePointsToFile() throws IOException
	{
		File file = new File(outputFileName);
		
		//if sorting by x, just write the points straight down
		if(sortByAngle == false){
			FileWriter fw = new FileWriter(file);
			fw.write(this.toString());
			fw.flush();
			fw.close();
		}
		
		//otherwise, print in this format for mathmatica.
		if(sortByAngle == true){
			FileWriter fw = new FileWriter(file);
			
			fw.write(lowestPoint.getX() + " " + lowestPoint.getY()+ "\n");
			for (int i = 0; i<points.length; i++){
				fw.write(points[i].getX() + " " + points[i].getY()+" ");
				fw.write(lowestPoint.getX() + " " + lowestPoint.getY()+" ");
				fw.write(points[i].getX() + " " + points[i].getY());
				fw.write("\n");
			}
			
			
			fw.flush();
			fw.close();
		}
	}	


	/**
	 * Generates a comparator on the fly that compares by polar angle if sortByAngle == true
	 * and by x-coordinate if sortByAngle == false. Set the protected variable pointComparator
	 * to it. Need to create an object of the PolarAngleComparator class and call the compareTo() 
	 * method in the Point class, respectively for the two possible values of sortByAngle.  
	 * 
	 * @param order
	 */
	protected void setComparator() 
	{
		
		
		
		if (!sortByAngle) {
			pointComparator = new Comparator<Point>() {
				public int compare(Point p, Point q) {
					return p.compareTo(q);
				}
			};
		} else {
			pointComparator = new PolarAngleComparator(lowestPoint);
		}
		
	}

	
	/**
	 * Swap the two elements indexed at i and j respectively in the array points[]. 
	 * 
	 * @param i
	 * @param j
	 */
	protected void swap(int i, int j)
	{
		Point temp;
		temp = points[i];
		points[i] = points[j];
		points[j] = temp;
	}	
}



