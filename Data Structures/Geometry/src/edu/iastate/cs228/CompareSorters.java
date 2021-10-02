package edu.iastate.cs228;

/**
 *  
 * @author James Taylor
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Perform the four sorting algorithms over each sequence of
	 * integers, comparing points by x-coordinate or by polar angle with respect
	 * to the lowest point.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws InputMismatchException
	 **/
	public static void main(String[] args)
			throws InputMismatchException, FileNotFoundException {


		int numTrials = 0; int order; 
		Random rand = new Random();
		AbstractSorter[] sorters = new AbstractSorter[4];
		Point[] points;
		Scanner scan = new Scanner(System.in);
		String fileName;

		System.out.println("Comparison of Four Sorting Algorithms");
		System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
		System.out.println("order: 1 (by x-coordinate) 2 (by polar angle)");
		
		numTrials++;
		System.out.print("\nTrial " + numTrials + " => Enter key: ");
		int key = scan.nextInt();
		int flag = 0;
		
		while (key!=3) {
			
			if (flag==0){
			flag = 1;
			} else numTrials++;
			
		   if (numTrials>1){
			System.out.print("\nTrial " + numTrials + " => Enter key: ");
			key = scan.nextInt();
		   }
		   
			if (key == 1) {	
			System.out.print("Enter number of random points: ");
				
				int numPoint = scan.nextInt();
				points = generateRandomPoints(numPoint, rand);
				sorters[0] = new SelectionSorter(points);
				sorters[1] = new InsertionSorter(points);
				sorters[2] = new MergeSorter(points);
				sorters[3] = new QuickSorter(points);	
			} else if (key == 2) {
				System.out.print("File name: ");
				fileName = scan.next();//(TEXT) FILE MUST BE IN THE PACKAGE.
				sorters[0] = new SelectionSorter(fileName);
				sorters[1] = new InsertionSorter(fileName);
				sorters[2] = new MergeSorter(fileName);
				sorters[3] = new QuickSorter(fileName);						
			} else{
				scan.close();
				return;
			}
			    		
		System.out.print("Order used in sorting: ");
		order = scan.nextInt();
		System.out.println();

		for (int i = 0; i < sorters.length; i++) {
			sorters[i].sort(order);
		}
		System.out.println("algorithm           size        time (ns) ");
		System.out.println("---------------------------------------------");
		System.out.println();
		for (int i = 0; i < sorters.length; i++) {
			System.out.println(sorters[i].stats());
		}
		System.out.println("---------------------------------------------");
		}
	}
	

	/**
	 * This method generates a given number of random points to initialize
	 * randomPoints[]. The coordinates of these points are pseudo-random numbers
	 * within the range [-50,50] × [-50,50]. Please refer to Section 3 of
	 * assignment description document on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts
	 *            number of points
	 * @param rand
	 *            Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException
	 *             if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand)
			throws IllegalArgumentException {
		Point[] temp = new Point[numPts];
		for (int i = 0; i < numPts; i++) {
			temp[i] = new Point(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
		}
		return temp;
	}
}
