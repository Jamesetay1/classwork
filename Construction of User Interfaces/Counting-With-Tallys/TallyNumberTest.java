package mini1;

public class TallyNumberTest {
	 public static void main(String[] args)
	 {
		 //TallyNumber tn = new TallyNumber("||**|*|");
		 //int value = tn.evaluateGroup("||**|*|");
		 //System.out.println(value); // expected 19
		 
		// TallyNumber tn1 = new TallyNumber("* ||");
		 //int value1 = tn1.getIntValue();
		// System.out.println(value1); // expected 19
		 
		 //TallyNumber tn2 = new TallyNumber(17);
		 //tn2.normalize();
		 //System.out.println(tn2.getStringValue());
		 /*TallyNumber t5 = new TallyNumber("| *");
		 TallyNumber t6 = new TallyNumber("| ** 0 ||");
		 t6.combine(t5);
		System.out.println(t6.getIntValue()); // 2017
		System.out.println(t6.getStringValue()); // "| ** |0 *||"*/
		
		 TallyNumber t1 = new TallyNumber("*** 0 0 ||");
		 TallyNumber t2 = new TallyNumber("** ** || |||| *");
		 t1.combine(t2);
		 System.out.println(t1.getIntValue()); // 197
		 System.out.println(t1.getStringValue()); // "***|||| ||*"

//		 TallyNumber t3 = new TallyNumber("| *");
//		 TallyNumber t4 = new TallyNumber("| ** 0 ||");
//		 t3.combine(t4);
//		 System.out.println(t3.getIntValue()); // 2017
//		 System.out.println(t3.getStringValue()); // "| ** |0 *||"  
		 
	/* System.out.println(tn.getIntValue()); // expected 19
	 tn.normalize();
	 System.out.println(tn.getStringValue()); // expected "| *||||" */
	 }
}
