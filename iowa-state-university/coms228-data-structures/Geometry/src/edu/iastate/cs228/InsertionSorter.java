package edu.iastate.cs228;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.lang.IllegalArgumentException; 


/**
 *  
 * @author James Taylor
 *
 */

/**
 * 
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter 
{
	// Other private instance variables if you need ... 
	
	/**
	 * The two constructors below invoke their corresponding superclass constructors. They
	 * also set the instance variables algorithm and outputFileName in the superclass.
	 */

	/**
	 * Constructor takes an array of points. 
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		super(pts);
		algorithm = "insertion sort";
		outputFileName = "insert.txt";
	}	

	
	/**
	 * Constructor reads points from a file. 
	 * 
	 * @param inputFileName  name of the input file
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException 
	 */
	public InsertionSorter(String inputFileName) throws InputMismatchException, FileNotFoundException 
	{
		super(inputFileName);
		algorithm = "insertion sort";
		outputFileName = "insert.txt";
	}
	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 * 
	 * @param order  1   by x-coordinate 
	 * 			     2   by polar angle 
	 */
	@Override 
	public void sort(int order)
	{
		long start = System.nanoTime();
		if(order == 1)sortByAngle=false;
		if(order == 2)sortByAngle=true;
		setComparator();
		int n = points.length;	
		//outer for loop looks through whole thing one a time, inner starts at next one and inserts the next index in its proper place
		for (int i = 1; i< n; i++){
			for (int j = i; j>0; j--){
				
				if (pointComparator.compare(points[j],points[j-1]) == -1){
					swap(j, j-1);
				} else break;
			}
		}
		
		sortingTime = System.nanoTime() - start;
		
		
	}		
}
