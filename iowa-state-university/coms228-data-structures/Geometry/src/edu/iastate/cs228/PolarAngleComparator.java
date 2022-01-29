package edu.iastate.cs228;

/**
 *  
 * @author James Taylor
 *
 */

import java.util.Comparator;

/**
 * 
 * This class compares two points p1 and p2 by polar angle with respect to a reference point.  
 * It is known that the reference point is not above either p1 or p2, and in the case that
 * either or both of p1 and p2 have the same y-coordinate, not to their right. 
 *
 */
public class PolarAngleComparator implements Comparator<Point>
{
	private Point referencePoint; 
	
	/**
	 * 
	 * @param p reference point
	 */
	public PolarAngleComparator(Point p)
	{
		referencePoint = p; 
	}
	
	/**
	 * Use cross product and dot product to implement this method.  Do not take square roots 
	 * or use trigonometric functions. See the PowerPoint notes on how to carry out cross and 
	 * dot products. Calls private methods crossProduct() and dotProduct(). 
	 * 
	 * Call comparePolarAngle() and compareDistance(). 
	 * 
	 * @param p1
	 * @param p2
	 * @return  0 if p1 and p2 are the same point
	 *         -1 otherwise, if one of the following three conditions holds: 
	 *                a) p1 and referencePoint are the same point (hence p2 is a different point); 
	 *                b) neither p1 nor p2 equals referencePoint, and the polar angle of 
	 *                   p1 with respect to referencePoint is less than that of p2; 
	 *                c) neither p1 nor p2 equals referencePoint, p1 and p2 have the same polar 
	 *                   angle w.r.t. referencePoint, and p1 is closer to referencePoint than p2. 
	 *   
	 *          1  otherwise. 
	 *                   
	 */
	public int compare(Point p1, Point p2)
	{
		if (p1.equals(p2))return 0;
		
		if (p1.equals(referencePoint)) return -1;
		//p1 wrt p0 has a smaller polar angle
		else if (!p1.equals(referencePoint)&&!p2.equals(referencePoint)  && 
				comparePolarAngle(p1, p2) == -1) return -1;
		//Same polar angle, check distance
		else if (!p1.equals(referencePoint)&&!p2.equals(referencePoint) &&
				comparePolarAngle(p1, p2) == 0 && compareDistance(p1, p2) == -1) return -1;
		return 1; 
	}
	
	
	/**
	 * Compare the polar angles of two points p1 and p2 with respect to referencePoint.  Use 
	 * cross products.  Do not use trigonometric functions. 
	 * 
	 * Ought to be private but made public for testing purpose. 
	 * 
	 * @param p1
	 * @param p2
	 * @return    0  if p1 and p2 have the same polar angle.
	 * 			 -1  if p1 equals referencePoint or its polar angle with respect to referencePoint
	 *               is less than that of p2. 
	 *            1  otherwise. 
	 */
	//One big difference is that with cross product we could get 0 (i.e. vectors are parallel) 
	//as in slide 4, however with dot product we only check < or > as it 
	//cannot be equal to 0 (because this happens only when vectors are perpendicular,
	//but they are parallel in this case) as in slide 5.
	
	//if p1 is a bigger polar angle returns 1 (BUT CROSS PRODUCT WILL BE NEGATIVE)
    public int comparePolarAngle(Point p1, Point p2) 
    {
    	if (crossProduct(p1, p2) == 0) return 0; //cross products =, so does distance. Parallel
    	if (p1.equals(referencePoint)) return -1;   //if p1 equals referencePoint
    	if (crossProduct(p1, p2) > 0) return -1;   //if CP is positive, return -1.
    	//if (crossProduct(p1, p1) < crossProduct(p2,p2)) return -1; //OLD (COPY OF DOT?)
    	 return 1; //if (crossProduct(p1, p2) < 0) return 1; BECAUSE P1 IS BIGGER THAN P2. CP IS NEGATIVE.
    }
    
    
    /**
     * Compare the distances of two points p1 and p2 to referencePoint.  Use dot products. 
     * Do not take square roots. 
     * 
     * Ought to be private but made public for testing purpose.
     * 
     * @param p1
     * @param p2
     * @return    0   if p1 and p2 are equidistant to referencePoint
     * 			 -1   if p1 is closer to referencePoint 
     *            1   otherwise (i.e., if p2 is closer to referencePoint)
     */
    public int compareDistance(Point p1, Point p2)
    {
    	if (dotProduct(p1, p1) == dotProduct(p2,p2)) return 0; //dot products =, so does distance.
    	if (dotProduct(p1, p1) < dotProduct(p2,p2)) return -1; //dot products of p0 to p1 is less than p0 to p2
    	 return 1; //dot products of p0 to p1 is more than p0 to p2 (p0 to p1 is further)
    	
    }
    

    /**
     * 
     * @param p1
     * @param p2
     * @return cross product of two vectors p1 - referencePoint and p2 - referencePoint
     */
    private int crossProduct(Point p1, Point p2)
    {
    	int cp1;
    	cp1 = (p1.getX() - referencePoint.getX()) * (p2.getY() - referencePoint.getY()) -
    		  (p2.getX() - referencePoint.getX()) * (p1.getY() - referencePoint.getY());
    	
    	return cp1; 
    }

    /**
     * 
     * @param p1
     * @param p2
     * @return dot product of two vectors p1 - referencePoint and p2 - referencePoint
     */
    private int dotProduct(Point p1, Point p2)
    {
    	int dp1;
    	dp1 = (p1.getX() - referencePoint.getX()) * (p2.getX() - referencePoint.getX()) +
    		  (p1.getY() - referencePoint.getY()) * (p2.getY() - referencePoint.getY()); 
    	
    	return dp1; 
    }
}

//package edu.iastate.cs228;
//
///**
// *  
// * @author Michael Black
// *
// */
//
//import java.util.Comparator;
//
///**
// * 
// * This class compares two points p1 and p2 by polar angle with respect to a
// * reference point. It is known that the reference point is not above either p1
// * or p2, and in the case that either or both of p1 and p2 have the same
// * y-coordinate, not to their right.
// *
// */
//public class PolarAngleComparator implements Comparator<Point> {
//	private Point referencePoint;
//
//	/**
//	 * 
//	 * @param p
//	 *            reference point
//	 */
//	public PolarAngleComparator(Point p) {
//		referencePoint = p;
//	}
//
//	/**
//	 * Use cross product and dot product to implement this method. Do not take
//	 * square roots or use trigonometric functions. See the PowerPoint notes on
//	 * how to carry out cross and dot products. Calls private methods
//	 * crossProduct() and dotProduct().
//	 * 
//	 * Call comparePolarAngle() and compareDistance().
//	 * 
//	 * @param p1
//	 * @param p2
//	 * @return 0 if p1 and p2 are the same point -1 otherwise, if one of the
//	 *         following three conditions holds: a) p1 and referencePoint are
//	 *         the same point (hence p2 is a different point); b) neither p1 nor
//	 *         p2 equals referencePoint, and the polar angle of p1 with respect
//	 *         to referencePoint is less than that of p2; c) neither p1 nor p2
//	 *         equals referencePoint, p1 and p2 have the same polar angle w.r.t.
//	 *         referencePoint, and p1 is closer to referencePoint than p2.
//	 * 
//	 *         1 otherwise.
//	 * 
//	 */
//	public int compare(Point p1, Point p2) {
//		if (p1.compareTo(p2) == 0) {
//			return 0;
//		} else if ((p1.compareTo(referencePoint) == 0) || (comparePolarAngle(p1, p2) == -1)
//				|| ((comparePolarAngle(p1, p2) == 0) && (compareDistance(p1, p2) == -1))) {
//			return -1;
//		} else {
//			return 1;
//		}
//	}
//
//	/**
//	 * Compare the polar angles of two points p1 and p2 with respect to
//	 * referencePoint. Use cross products. Do not use trigonometric functions.
//	 * 
//	 * Ought to be private but made public for testing purpose.
//	 * 
//	 * @param p1
//	 * @param p2
//	 * @return 0 if p1 and p2 have the same polar angle. -1 if p1 equals
//	 *         referencePoint or its polar angle with respect to referencePoint
//	 *         is less than that of p2. 1 otherwise.
//	 */
//	public int comparePolarAngle(Point p1, Point p2) {
//		int result = crossProduct(p1, p2);
//		if ((result > 0) || p1.compareTo(referencePoint) == 0) {
//			return -1;
//		} else if (result == 0 && p2.compareTo(referencePoint) != 0) {
//			return 0;
//		} else {
//			return 1;
//		}
//	}
//
//	/**
//	 * Compare the distances of two points p1 and p2 to referencePoint. Use dot
//	 * products. Do not take square roots.
//	 * 
//	 * Ought to be private but made public for testing purpose.
//	 * 
//	 * @param p1
//	 * @param p2
//	 * @return 0 if p1 and p2 are equidistant to referencePoint -1 if p1 is
//	 *         closer to referencePoint 1 otherwise (i.e., if p2 is closer to
//	 *         referencePoint)
//	 */
//	public int compareDistance(Point p1, Point p2) {
//		int result = dotProduct(p1, p1) - dotProduct(p2, p2);
//		if (result < 0) {
//			return -1;
//		} else if (result > 0) {
//			return 1;
//		} else {
//			return 0;
//		}
//	}
//
//	/**
//	 * 
//	 * @param p1
//	 * @param p2
//	 * @return cross product of two vectors p1 - referencePoint and p2 -
//	 *         referencePoint
//	 */
//	private int crossProduct(Point p1, Point p2) {
//		int x1 = p1.getX() - referencePoint.getX();
//		int y1 = p1.getY() - referencePoint.getY();
//		int x2 = p2.getX() - referencePoint.getX();
//		int y2 = p2.getY() - referencePoint.getY();
//		return x1 * y2 - x2 * y1;
//	}
//
//	/**
//	 * 
//	 * @param p1
//	 * @param p2
//	 * @return dot product of two vectors p1 - referencePoint and p2 -
//	 *         referencePoint
//	 */
//	private int dotProduct(Point p1, Point p2) {
//		int x1 = p1.getX() - referencePoint.getX();
//		int y1 = p1.getY() - referencePoint.getY();
//		int x2 = p2.getX() - referencePoint.getX();
//		int y2 = p2.getY() - referencePoint.getY();
//		return x1 * x2 + y1 * y2;
//	}
//}
