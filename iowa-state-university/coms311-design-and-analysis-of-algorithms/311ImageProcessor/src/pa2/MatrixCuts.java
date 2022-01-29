package pa2;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Matrix Cuts Class
 *
 * @author Zach DeMaris
 * @author James Taylor
 */
public class MatrixCuts {

    /**
     *
     * @param M
     * @return the min-cost width cut of M and its cost
     * The first tuple is <totalcost, -1> and the rest
     * of the tuples are a list of width cuts <i,j>
     */
    @SuppressWarnings("Duplicates")
    public static ArrayList<Tuple> widthCut(int[] [] M){

    	//First contstruct secondary array
    	int[][] computedArr = new int[M.length][M[0].length];

    	//Iterate through M computer optimal choice for each cell.
    	for(int i = 0; i < M.length; i++) {
    		for(int j = 0; j < M[0].length; j++) {

    			int aboveLeft = Integer.MAX_VALUE;
    			int above = Integer.MAX_VALUE;
    			int aboveRight = Integer.MAX_VALUE;


    			//Check if above left is valid
        		if(isValidIndex(i - 1,j - 1, M)) {
        			aboveLeft = computedArr[i - 1][j - 1] + M[i][j];
        		}

        		//Check if above is valid
        		if(isValidIndex(i - 1,j, M)) {
        			above = computedArr[i - 1][j]  + M[i][j];
        		}

        		//Check if above right is valid
        		if(isValidIndex(i - 1, j + 1, M)) {
        			aboveRight = computedArr[i - 1][j + 1]  + M[i][j];
        		}

        		//Choose the minimum cost path to the current i,j
        		int minPath = threeArgMin(aboveLeft, above, aboveRight);

        		if(i == 0) {
        			minPath = M[i][j];
        		}

        		//Update the computed array to the minimum cost path for this i,j
        		computedArr[i][j] = minPath;
    		}
    	}

    	System.out.print("computed Array to cut:" + Arrays.deepToString(computedArr) + "\n");

    	int minBottom = Integer.MAX_VALUE;

    	//Loop through bottom row right to left
        int minBottomIndex = computedArr[0].length-1;
        for(int j=computedArr[0].length-1; j>=0; j--){
            if(computedArr[computedArr.length-1][j] <= minBottom){
                minBottom=computedArr[computedArr.length-1][j];
                minBottomIndex = j;

            }
        }
        int runningCost = computedArr[computedArr.length-1][minBottomIndex];

        ArrayList<Tuple> tupleList = new ArrayList<>();
        tupleList.add(new Tuple(computedArr.length-1, minBottomIndex));



        //Work from bottom to top following minimum cost
        //and adding that to a list
        int i = computedArr.length-1;
        int j = minBottomIndex;


        while(i>0){
           //Check if things exist
            int costLeftAbove = Integer.MAX_VALUE;
            int costStraightAbove = Integer.MAX_VALUE;
            int costRightAbove = Integer.MAX_VALUE;

            if(isValidIndex(i-1,j-1, computedArr)){
                costLeftAbove = computedArr[i-1][j-1];
            }
            if(isValidIndex(i-1,j,computedArr)){
                costStraightAbove = computedArr[i-1][j];
            }
            if(isValidIndex(i-1,j+1, computedArr)){
                costRightAbove = computedArr[i-1][j+1];
            }
            int minCostAbove = threeArgMin(costLeftAbove, costStraightAbove, costRightAbove);

            if(minCostAbove==costLeftAbove){
                i--;
                j--;
            }else if(minCostAbove==costStraightAbove){
                i--;
            }else if(minCostAbove==costRightAbove){
                i--;
                j++;
            }
            tupleList.add(0, new Tuple(i,j));
        }

        tupleList.add(0, new Tuple(runningCost, -1));
        System.out.println("TupleList: " + tupleList);

        return tupleList;
    }

    /**
     *
     * @param M
     * @return the min-cost stitch cut of M and its cost
     * The first tuple is <totalcost, -1> and the rest
     * of the tuples are a list of stitch cuts <i,j>
     */
    @SuppressWarnings("Duplicates")
    public static ArrayList<Tuple> stitchCut(int[][] M){

        //First contstruct secondary array
        int[][] computedArr = new int[M.length][M[0].length];

        //Iterate through M computer optimal choice for each cell.
        for(int i = 0; i < M.length; i++) {
            for(int j = 0; j < M[0].length; j++) {

                int aboveLeft = Integer.MAX_VALUE;
                int above = Integer.MAX_VALUE;
                int aboveRight = Integer.MAX_VALUE;


                //Check if above left is valid
                if(isValidIndex(i ,j - 1, M)) {
                    aboveLeft = computedArr[i][j - 1] + M[i][j];
                }

                //Check if above is valid
                if(isValidIndex(i - 1,j-1, M)) {
                    above = computedArr[i - 1][j - 1]  + M[i][j];
                }

                //Check if above right is valid
                if(isValidIndex(i - 1, j, M)) {
                    aboveRight = computedArr[i - 1][j]  + M[i][j];
                }

                //Choose the minimum cost path to the current i,j
                int minPath = threeArgMin(aboveLeft, above, aboveRight);

                if(i == 0) {
                    minPath = M[i][j];
                }

                //Update the computed array to the minimum cost path for this i,j
                computedArr[i][j] = minPath;
            }
        }

        System.out.print(Arrays.deepToString(computedArr)+ "\n");

        int minBottom = Integer.MAX_VALUE;

        //Loop through bottom row right to left
        int minBottomIndex = computedArr[0].length-1;
        for(int j=computedArr[0].length-1; j>=0; j--){
            if(computedArr[computedArr.length-1][j] <= minBottom){
                minBottom=computedArr[computedArr.length-1][j];
                minBottomIndex = j;
            }
        }
        int runningCost = computedArr[computedArr.length-1][minBottomIndex];

        ArrayList<Tuple> tupleList = new ArrayList<>();
        tupleList.add(new Tuple(computedArr.length-1, minBottomIndex));

        //Work from bottom to top following minimum cost
        //and adding that to a list
        int i = computedArr.length-1;
        int j = minBottomIndex;
        while(i>0){
            //Check if things exist
            int costLeft= Integer.MAX_VALUE;
            int costLeftAbove = Integer.MAX_VALUE;
            int costStraightAbove = Integer.MAX_VALUE;

            if(isValidIndex(i,j-1, computedArr)){
                costLeft = computedArr[i][j-1];
            }
            if(isValidIndex(i-1,j-1,computedArr)){
                costLeftAbove = computedArr[i-1][j-1];
            }
            if(isValidIndex(i-1,j, computedArr)){
                costStraightAbove = computedArr[i-1][j];
            }
            int minCostAbove = threeArgMin(costLeft, costLeftAbove, costStraightAbove);
            if(minCostAbove==costLeft){
                j--;
            }else if(minCostAbove==costLeftAbove){
                i--;
                j--;
            }else if(minCostAbove==costStraightAbove){
                i--;
            }
            tupleList.add(0, new Tuple(i,j));
        }

        tupleList.add(0, new Tuple(runningCost, -1));


        return tupleList;
    }


    /**
     * Checks whether a given row,col index is valid for arr[][]
     *
     * @param row index to row of array
     * @param col index to column of array
     * @param arr array we testing out of bounds on
     * @return true if valid row,column index otherwise false
     */
    private static boolean isValidIndex(int row, int col, int[][] arr) {
    	//Check if element is too small of index
    	if(row < 0 || col < 0) {
    		return false;
    	}
    	if(row >= arr.length) {
    		return false;
    	}
    	if(col >= arr[0].length) {
    		return false;
    	}

    	return true;
    }

    private static int threeArgMin(int x, int y, int z) {
    	return Math.min(Math.min(x, y), z);
    }
    @SuppressWarnings("Duplicates")
    public static void main(String args[]){

        ArrayList tuplelist;
        ArrayList tuplelist2;
//        int[][] arr1 = {
//                {5,7,9,4,5},
//                {2,3,1,1,2},
//                {2,0,49,46,8},
//                {3,1,1,1,1},
//                {50,51,25,26,1}
//        };
//
//        tuplelist = widthCut(arr1);
//        System.out.println(tuplelist);
//
//        tuplelist2 = stitchCut(arr1);
//        System.out.println(tuplelist2+"\n");
//
        int[][] arr2 = {
                {1,100,100,100},
                {100,5,0,100},
                {100,0,1,100},
                {100,100,100,6}
        };

        tuplelist = widthCut(arr2);
        System.out.println(tuplelist);

        tuplelist2 = stitchCut(arr2);
        System.out.println(tuplelist2+"\n");

//        int[][] arr3 = {
//                {10,5,8,9,12},
//                {4,4,4,4,4},
//                {100,100,1,100,100},
//                {100,100,3,100,100}
//        };
//
//        tuplelist = widthCut(arr3);
//        System.out.println(tuplelist);
//
//        tuplelist2 = stitchCut(arr3);
//        System.out.println(tuplelist2+"\n");
//
//                int[][] arr1 = {
//                {81,1,400,400,400},
//                {100,81,1,100,100},
//                {100,81,1,100,100},
//                {0,100,81,1,400},
//                        {100,81,1,100,100},
//                {0,100,81,1,400}
//        };
//
//        ArrayList tuplelist = widthCut(arr1);
//        System.out.println(tuplelist);
//
////        ArrayList tuplelist2 = stitchCut(arr1);
////        System.out.println(tuplelist2+"\n");
    }
}
