package edu.iastate.cs228.hw1;

/**
 * @author
 * James Taylor || COM S 228, Iowa State University || 9/8/2017         
*/
public class ProteinSequence extends Sequence
{
	/**
	 * calls super to construct a new Protein Sequence for our sequence.
	 * @param psarr 
	 * 		the new sequence that will be made and saved into seqarr 
	 * @throws IllegalArgumentException 
   * 			if any of this classes isValidLetter returns false (in this case, if it ,b,B,j,J,o,O,U,U,x,X,or z,Z appear).
	 */
	public ProteinSequence(char[] psarr)
	{
		super(psarr);
	}

	/**
	 * Takes in character aa and checks to make sure it is not b,B,j,J,o,O,u,U,x,X,or z,Z.
	 * If so, return false.
	 * @param aa the character that will be checked.
	 * @return if it doesn't contain one of those letter, return true. If it does, false.
	 * 		
	 */
	@Override
	public boolean isValidLetter(char aa)
	{

		if (((aa == 'b')||(aa == 'B')||
			(aa == 'j')||(aa == 'J')||
			(aa == 'o')||(aa == 'O')||
			(aa == 'u')||(aa == 'U')||
			(aa == 'x')||(aa == 'X')|| 
			(aa == 'z')||(aa == 'Z'))){return false;}
			return true;
		}
	}


