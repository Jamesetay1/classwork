package logic;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import api.Cell;
import api.Flow;

public class Tester {
	public static void main(String[] args) throws FileNotFoundException
	 {
//		 // create a simple game
//		 Flow[] flows = new Flow[3];
//		 flows[0] = new Flow(new Cell(0, 0, 'G'), new Cell(1, 2, 'G'));
//		 flows[1] = new Flow(new Cell(0, 1, 'R'), new Cell(0, 3, 'R'));
//		 flows[2] = new Flow(new Cell(2, 0, 'B'), new Cell(1, 3, 'B'));
//		 FlowGame game = new FlowGame(flows, 4, 3);
//		 // check the initial game state
//		 System.out.println(game.getWidth());
//		 System.out.println(game.getHeight());
//		 System.out.println(game.getCurrent());
//		
//		 Flow[] temp = game.getAllFlows();
//		 for (Flow f : temp)
//		 {
//		 System.out.println(f);
//		 }
//	 
		 
		 
	 System.out.println("----------------------------------------------------");
	 
//	 game.startFlow(1, 3);
//	 System.out.println(game.getCurrent());
//	 temp = game.getAllFlows();
//	 for (Flow f : temp)
//	 {
//	 System.out.println(f);
//	 }
	//  create a simple game
	 Flow[] flows = new Flow[3];
	 flows[0] = new Flow(new Cell(0, 0, 'G'), new Cell(1, 2, 'G'));
	 flows[1] = new Flow(new Cell(0, 1, 'R'), new Cell(0, 3, 'R'));
	 flows[2] = new Flow(new Cell(2, 0, 'B'), new Cell(1, 3, 'B'));
	 FlowGame game = new FlowGame(flows, 7, 5);
	 // check the initial game state
	 System.out.println(game.getWidth());
	 System.out.println(game.getHeight());
	 System.out.println(game.getCurrent());
	 Flow[] temp = game.getAllFlows();
	 game.startFlow(1, 3);
	 //game.startFlow(1, 2);
	 //game.startFlow(2, 0);
	 System.out.println(game.getCurrent());
//	 for (Flow f : temp)
//	 {
//	 System.out.println(f);
//	 }
	if (game.isOccupied(3,0)){
		System.out.println("7,5 is occupied");
	}
	if (game.isOccupied(2,1)){
		System.out.println("1,2 is not occupied");
	}
	
	 game.addCell(2, 2); // no change, cell is not adjacent to current
	 game.addCell(0, 3); // no change, cell is occupied
	 game.addCell(2, 3); // should add new cell to flow and update current
	 game.addCell(2, 2);
	 game.addCell(2, 1);
	 game.addCell(2, 0);
	 
	 System.out.println("should be null:" + game.getCurrent());
	 System.out.println("CurrentCell from Tester after adding:" + game.getCurrent());
	 System.out.println(game.getWidth());
	 System.out.println(game.getHeight());
	 System.out.println(game.getCurrent());
	 temp = game.getAllFlows();
	 for (Flow f : temp)
	 {
	 System.out.println(f);
	 }
	 System.out.println("should be null:" + game.getCurrent());
	 
	 System.out.println("----------------------------------------------------");
	 
	 game.addCell(2, 2); // no change, cell is not adjacent to current
	 game.addCell(0, 3); // no change, cell is occupied
	 game.addCell(2, 3); // should add new cell to flow and update current
	 System.out.println(game.getCurrent());
	 temp = game.getAllFlows();
	 for (Flow f : temp)
	 {
	 System.out.println(f);
	 }
	
	 
	 System.out.println("----------------------------------------------------");}
	 
//	 String[] testgrid = {
//			 "G-R-",
//			 "G-RC",
//			 "B--B",
//			 "Y--Y"
//			 
//			 };
//	 System.out.println(testgrid.getHeight());
//	 Util.createFlowsFromStringArray(testgrid);
	 
	 //Util.readFile("hw3testfile.txt");
	 
	 public void invalidGameFlowsHeightWidth(){
			FlowGame game = new FlowGame(null, 2, 2);
			assertTrue(null==game.getAllFlows());
 }
 }
	 

