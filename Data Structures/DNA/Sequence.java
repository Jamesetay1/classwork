package edu.iastate.cs228.hw1;


/**
 * @author
 * James Taylor || COM S 228, Iowa State University || 9/8/2017         
*/


public class Sequence
{
  public char[] seqarr; // made public instead of protected for grading.
  
  /**
   * Constructs a new sequence. Goes through every latter to check if it is valid,
   * and then saves it into seqarr.
   * 
   * @param sarr 
   * 			the new sequence that will be made and saved into seqarr 
   * @throws IllegalArgumentException 
   * 			if any of isValidLetter returns false (in this case, if it is not a letter).
   */
  public Sequence(char[] sarr) 
  {
	
	seqarr = new char[sarr.length];
	  
    for (int i = 0; i<sarr.length; i++){
    	if(isValidLetter(sarr[i])){	
    		seqarr[i]=sarr[i];		
    	}
    	else{
    		throw new IllegalArgumentException("Invalid sequence letter for " + this.getClass());	
    	}
    } 	
   }
  
/**
 * gets and returns the length of seqarr (our saved sequence)
 * @return the length of seqarr as an int.
 */
  public int seqLength()
  {
    return seqarr.length;
  }
  
  /**
   * gets and returns seqarr (our saved sequence)
   * @return seqarr (our saved sequence)
   */
  public char[] getSeq()
  {
	  return seqarr;
  }

 /**
  * Goes through the character array of seqarr and returns it as a string.
  * @return the string form of our sequence.
  */
  public String toString()
  {
	  String temp = "";
	  int i = 0;
	  
	  for (i=0; i<seqarr.length; i++){
		  temp+=seqarr[i];
	  }
    return temp;
  }

 /**
  * Checks to make sure our object is the same as the one being testing against it.
  * @return if the objects are equal or not (bool)
  */
  public boolean equals(Object obj)//TO DO - TEST
  { 
	  String coming = obj.toString();
	    String our = this.toString();
    if (obj == null || !obj.getClass().equals(this.getClass())){return false;}
    else if (our.equals(coming))return true;
    return false;
   

  }

  /**
   * takes in a character and checks if it is upper or lowercase (if it is a letter or not)
   * @param let 
   * 	the letter we are checking
   * @return
   * 	if let is indeed a letter or not (bool)
   */
  public boolean isValidLetter(char let)
  {
	  if (Character.isUpperCase(let) || Character.isLowerCase(let)){
	 return true;
	  } else return false;
  }

}
