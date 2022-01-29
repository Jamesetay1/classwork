package mini1;

import java.util.Arrays;

//import java.util.Arrays;
//The constructor which takes a String should save that string and calculate the int value. 
//The constructor given the int value should save the int value and determine the string representation.

//Ask Steve how this works on Piazza
/* if (!givenString.matches("[\\ \\*\\|\\0]*")){
 	tallyInt = -1;
}*/

public class TallyNumber {
	private String tallyString = "";
	private int tallyInt = 0;

	public TallyNumber(java.lang.String givenString){
		tallyString = givenString;
        if (tallyString == ""){
        	tallyInt = -1;
        }
        else if (tallyString == null){
        	tallyInt = -1;
        }
        
        else if (isInvalid(tallyString)){
        	tallyInt=-1;
        }
        else tallyInt = evaluateString(tallyString);
        
        if (tallyInt ==-1)
        		tallyString = "";
	}
	//Helper Method to see if invalid character
        private boolean isInvalid(String givenString){
        	
        	for (int i = 0; i <= givenString.length()-1; i++){
        		char c = givenString.charAt(i);
        		if (!(c == '0' || c == '*' || c == '|' || c == ' ')){
        			return true;
        		}
        	}
        			return false;
        }
    public TallyNumber(int givenValue){
		
    	tallyInt = givenValue;
    	normalize();
		if (givenValue < 0)
			tallyInt = -1;	
		else if (tallyInt == 0){
			tallyString = "0";
		}
		
	}
	
public String getStringValue(){
		
		//normalize();
		return tallyString;
	}

	public int getIntValue(){
		
		return tallyInt;
	}

	private int evaluateString(java.lang.String stringToEval){
		String tempToEval = "";
		int[] Clusters = new int [100];
		int count=0;
		for(int j=0;j<Clusters.length;j++){Clusters[j]= -1;}
		
		for (int i = 0; i<=stringToEval.length()-1; i++){
			if (stringToEval.charAt(i)!=(' ')){
				tempToEval += stringToEval.charAt(i);
			}

			if (stringToEval.charAt(i)==(' ')||i==stringToEval.length()-1){
				Clusters[count]= evaluateGroup(tempToEval);
				count++;
				tempToEval = "";		
			}
		}	
		int n = -1;
		while (Clusters[n+1] != -1){
			tallyInt = (tallyInt *10)+ Clusters[n+1];
			n++;
		}
		
		return tallyInt;
		
	}

	//Helper Method to evaluate groups before returning in getIntValue
	private int evaluateGroup(java.lang.String groupToEval){
		int groupValue = 0;
		for (int i = 0; i <= groupToEval.length()-1; i++){
			if (groupToEval.charAt(i)==('0'))groupValue+=0;
			if (groupToEval.charAt(i)==('|'))groupValue+=1;
			if (groupToEval.charAt(i)==('*'))groupValue+=5;
		}
		return groupValue;
	}

	public void add(TallyNumber other){
		int tallyOther = other.tallyInt;
		tallyInt += tallyOther;
		normalize();
	}

	public void combine(TallyNumber other){
		tallyInt += other.tallyInt;
		System.out.println("at beginning of combine:  "+ tallyInt);
		String string1 = tallyString;
		String string2 = other.tallyString;
		
		String[] s1Split = string1.split(" ");
		String[] s2Split = string2.split(" ");
		
		System.out.println(Arrays.toString(s1Split));
		System.out.println(Arrays.toString(s2Split));
		
		int s1L = s1Split.length;
		int s2L = s2Split.length;
		
		
		String newString ="";
	
		//1 of 3 cases, if the first String is bigger
		if (s1L>s2L){
			int s1Index = 0;
			int s2Index =0;
			while (s1L>s2L){
				newString += s1Split[s1Index];
				newString += " ";
				s1L--;
				s1Index++;
				System.out.println(newString);
			}
			while (s1L==s2L && s2L>0){
				newString += s1Split[s1Index];
				newString += s2Split[s2Index];
				newString += " ";
				s1L--;
				s2L--;
				s1Index++;
				s2Index++;
				System.out.println(newString);
			
			}
		}	
		//2 of 3 cases, if the 2nd string is bigger
		else if (s1L<s2L){
				int s1Index = 0;
				int s2Index =0;
				while (s1L<s2L){
					newString += s2Split[s2Index];
					newString += " ";
					s2L--;
					s2Index++;
					System.out.println(newString);
				}
				while (s1L==s2L && s2L>0){
					newString += s1Split[s1Index];
					newString += s2Split[s2Index];
					newString += " ";
					s1L--;
					s2L--;
					s1Index++;
					s2Index++;				
				}
		}
		else{
				int s1Index = 0;
				int s2Index =0;
			while (s1L == s2L && s1L > 0){				
				System.out.println(newString);
				newString += s1Split[s1Index];
				System.out.println(newString);
				newString += s2Split[s2Index];
				newString += " ";
				System.out.println(newString);
				s1L--;
				s2L--;
				s1Index++;
				s2Index++;
			}
		}
			
			newString = newString.trim();
			System.out.println(newString);
			tallyString = newString;
	}


	public void normalize (){
		int TN = getIntValue();
		
		String normalizedString = "";
		while (TN >0){
			normalizedString = makeGroup(TN%10) + normalizedString;
			TN= TN/10;
			if (TN>0) {normalizedString =  " " + normalizedString;}
		}
		tallyString = normalizedString;
		
		
	}

	private String makeGroup(int intToGroup){
		String stringGrouped = "";
		
		if (intToGroup == 0){
			stringGrouped = "0";
			return stringGrouped;
		}
		else{
			if (intToGroup >=5){
				stringGrouped += "*";
				intToGroup -=5;
			}
			while (intToGroup > 0){
				stringGrouped += "|";
				intToGroup --;
			}
			
			return stringGrouped;

		}
	}
}