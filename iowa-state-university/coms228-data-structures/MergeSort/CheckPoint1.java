package lab7;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class CheckPoint1
{
  public static void main(String[] args)
  {
	int[] arr = {-17, -2, 4, 42,-3, 25, 1233, -12};
    System.out.println("Expect [4,42,25,1233]:   " + Arrays.toString(getPositiveNumbers(arr)));
    
    System.out.println(Arrays.toString(randomExperiement(10, 1000)));
    
    Deck deck = new Deck();
    Card[] hand = deck.select(10);
    System.out.println(Card.toString(hand));
  }
  public static int[] randomExperiement(int max, int iters){
	  Random rand = new Random();
	  int[] counts = new int[max+1];
	  int slot =0;
	  for (int i=0; i<iters; i++){
		  slot = rand.nextInt(max+1);
		  counts[slot] = counts[slot]+1;
	  }
	  return counts;
		  
	  
  }
  public static int[] getPositiveNumbers(int[] numbers){
	 int count = 0;
	  for (int i=0; i<numbers.length; i++){
		 if (numbers[i] > 0)
			 count ++;
		 System.out.println(count);
	  }
		 int[] result = new int[count];
		 int j = 0;
		 for (int i=0; i<numbers.length; i++){
			 if (numbers[i] > 0){
				 result[j] = numbers[i];
				 j++;
			 }
		 }
		 return result;
  }
 
}

