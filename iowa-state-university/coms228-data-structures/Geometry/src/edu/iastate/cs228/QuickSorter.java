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
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */

public class QuickSorter extends AbstractSorter
{
	
	// Other private instance variables if you need ... 
		
	/**
	 * The two constructors below invoke their corresponding superclass constructors. They
	 * also set the instance variables algorithm and outputFileName in the superclass.
	 */

	/** 
	 * Constructor accepts an input array of points. 
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts)
	{
		super(pts);
		algorithm = "quicksort";
		outputFileName = "quick.txt";
		
 
	}
		

	/**
	 * Constructor reads points from a file. 
	 * 
	 * @param inputFileName  name of the input file
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException 
	 */
	public QuickSorter(String inputFileName) throws InputMismatchException, FileNotFoundException 
	{
		super(inputFileName);
		algorithm = "quicksort";
		outputFileName = "quick.txt";
	}


	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
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
		setComparator();
		int n = points.length;
		quickSortRec(0, n-1);
		sortingTime = System.nanoTime()-start;
		
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last)
	{
		if (first>=last){
			return;
		}
		
		int split = partition(first,last);
		quickSortRec(first, split-1);
		quickSortRec(split+1, last);
		
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last)
	{
		Point pivot = points[last];
		int i = first-1; //setting index of smaller element.
		
		for (int j=first; j<last; j++){
			if (pointComparator.compare(points[j], pivot) != 1){
				i++;
				swap(i,j);
			}
		}
		
		swap(i+1, last);
		return i+1; 
	}	

}
