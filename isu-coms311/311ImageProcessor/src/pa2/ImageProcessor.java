package pa2;

import java.awt.*;
import java.util.*;

import static pa2.MatrixCuts.widthCut;


/**
 * Image Processing Class
 *
 * @author Zach DeMaris
 * @author James Taylor
 */
public class ImageProcessor {

	/**
     *
     * @param x
     * @param inputImage
     * @return Picture of reduced width
     */
    public static Picture reduceWidth(int x, String inputImage){

    	Picture picture = new Picture(inputImage);
		System.out.println(picture.toString());
    	for(int k = 0; k < x; k++) {

        	int width = picture.width();
        	int height = picture.height();
        	int[][] impMatrix = new int[height][width];

	    	// Compute the importance matrix
	    	for(int i = 0; i < height; i++) {
	    		for(int j = 0; j < width; j++) {
	    			impMatrix[i][j] = computeImportance(i, j, picture);
	    		}
	    	}
	    	System.out.println("Importance matrx: "+ Arrays.deepToString(impMatrix));
	    	ArrayList<Tuple> toCut = widthCut(impMatrix);
	    	int newWidth = width-1;
	    	Picture newPicture = new Picture(newWidth, height);

	    	//now for every pixel move the stuff over in a new picture
			//unless it is the pixel to cut, then skip it.
			boolean rowShift = false;
	    	for(int i = 0; i < height; i++){
				for(int j = 0; j < newWidth; j++){
					if(toCut.get(i+1).getX() == i && toCut.get(i+1).getY() == j) {
						rowShift = true;
					}
					if(rowShift == false) {
						newPicture.set(j, i, picture.get(j, i));
					}else{
						newPicture.set(j,i,picture.get(j+1, i));
					}
				}
				rowShift = false;
			}

	    	picture = newPicture;
    	}

        return picture;
    }

	private static int computeImportance(int row, int col, Picture picture) {

		if (col == 0) {
			return dist(picture.get(col, row), picture.get(col+1, row));
		} else if (col == (picture.width()-1)) {
			return dist(picture.get(col, row), picture.get(col-1, row));
		} else {
			return dist(picture.get(col-1, row), picture.get(col+1, row));
		}
	}

	private static int dist(Color p, Color q) {
		return (int) (Math.pow((p.getRed() - q.getRed()), 2) + Math.pow((p.getGreen() - q.getGreen()), 2)
				+ Math.pow((p.getBlue() - q.getBlue()), 2));
	}

  public static void main(String args[]){
//      String iastate1 = "http://web.cs.iastate.edu/~pavan/311/F19/images/iastate1.jpg";
//      Picture reducedBy1 = reduceWidth(1, iastate1);
//      Picture reducedBy10 = reduceWidth(10,iastate1);
//      Picture reducedBy100 = reduceWidth(100,iastate1);

//      String tinySteveTest = "http://web.cs.iastate.edu/~smkautz/cs311f19/temp3/test6x5.png ";
//      Picture reducedBy1 = reduceWidth(1, tinySteveTest);

//      String piazzaTest = "https://labs.jensimmons.com/2016/examples/images/testscreen-large.jpg";
//	  Picture reducedBy10 = reduceWidth(10, piazzaTest);
//	  reducedBy10.show();


//	  String pixilArt1 = "pixil-frame-0.png";
//	  Picture reducedBy1 = reduceWidth(1, pixilArt1);
//	  reducedBy1.show();

	  String pixilArt2 = "pixil-frame-0 (9).png";
	  Picture reducedBy1 = reduceWidth(2, pixilArt2);
	  System.out.println(reducedBy1.toString());
	  reducedBy1.show();
  }
}
