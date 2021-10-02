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
 * This class implements selection sort.   
 *
 */

public class SelectionSorter extends AbstractSorter
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
	public SelectionSorter(Point[] pts)  
	{
		super(pts);
		super.algorithm = "selection sort";
		super.outputFileName = "select.txt";
		
	}	

	
	/**
	 * Constructor reads points from a file. 
	 * 
	 * @param inputFileName  name of the input file
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException 
	 */
	public SelectionSorter(String inputFileName) throws InputMismatchException, FileNotFoundException 
	{
		super(inputFileName);
		super.algorithm = "selection sort";
		super.outputFileName = "select.txt";
	}
	
	
	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.  
	 *
	 * @param order  1   by x-coordinate 
	 * 			     2   by polar angle 
	 *
	 */
	@Override 
	public void sort(int order)
	{
		long start = System.nanoTime();
		if(order == 1)sortByAngle=false;
		if(order == 2)sortByAngle=true;
		setComparator();//set what comparator we are using based on order given.

			int n = points.length;
			
			//Move through unsorted array
			for (int i = 0; i<n-1; i++)
			{
				//Find smallest x.
				int mindex = i;
				for (int j = i+1; j<n; j++){
					if (pointComparator.compare(points[mindex], points[j])==1){//compares based on order given.
						mindex=j;
					}
				}
				super.swap(mindex, i);
			}
			sortingTime = System.nanoTime() - start;

		

	}	
}
