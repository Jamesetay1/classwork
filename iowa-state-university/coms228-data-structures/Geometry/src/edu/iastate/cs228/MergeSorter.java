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
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	
	/**
	 * The two constructors below invoke their corresponding superclass constructors. They
	 * also set the instance variables algorithm and outputFileName in the superclass.
	 */

	/** 
	 * Constructor accepts an input array of points. 
	 * in the array. 
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
		algorithm = "mergesort";
		outputFileName = "merge.txt"; 
	}
	
	
	/**
	 * Constructor reads points from a file. 
	 * 
	 * @param inputFileName  name of the input file
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException 
	 */
	public MergeSorter(String inputFileName) throws InputMismatchException, FileNotFoundException 
	{
		super(inputFileName);
		algorithm = "mergesort";
		outputFileName = "merge.txt";

	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
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
		mergeSortRec(points);
		sortingTime = System.nanoTime() - start;
		
	
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		int n = pts.length;
		int half = n/2;
		if (n>1)
		{

			Point[] Left = Arrays.copyOfRange(pts.clone(), 0, half);//here the arrays are split in half
			Point[] Right = Arrays.copyOfRange(pts.clone(), half, n);
			
			mergeSortRec(Left);//sort left half
			mergeSortRec(Right);//and the right half
			
			merge(Left, Right, pts); //merge back together, put in pts
;
		}
	}

	
/**
 * Merges L and R into F. Used in last step of recursive merge above.
 * @param L - The left half of the array
 * @param R - The Right half of the array
 * @param F - The array we are merging the two in to.
 */

	private void merge(Point[] L, Point[] R, Point[] F){
		
		int le = 0; int ri = 0; int k = 0;
		
		while(le < L.length && ri < R.length){
			if (pointComparator.compare(L[le], R[ri]) == -1){
				F[k] = L[le];
				le++;
			}
			else{F[k] = R[ri];
					 ri++;
			}
			k++;
		
		}
		
		if (le >= L.length){
			while (ri < R.length){
				F[k]= R[ri];
				ri++;
				k++;
				
			}
		}
		
		if (ri >= R.length){
			while(le < L.length){
				F[k]=L[le];
				le++;
				k++;
			}
		}
		
		
	}
	

	
}
