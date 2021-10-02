package logic;

import org.junit.*;
import static org.junit.Assert.*;
import api.*;
import java.util.ArrayList;
import java.io.FileNotFoundException;


public class FlowGameTests {
	
	private ArrayList<FlowGame> games;
	
	@Before
	public void setup() throws FileNotFoundException {
		games = Util.readFile("games.txt");
	}
	
	@Test
	public void nullCurrentCell(){
		for(FlowGame game : games){
			assertEquals(null, game.getCurrent());
		}
	}
	
	@Test
	public void invalidGameFlowsHeightWidth(){
		FlowGame game = new FlowGame(null, 2, 2);
		assertTrue(null==game.getAllFlows());
		assertEquals(0, game.getHeight());
		assertEquals(0, game.getWidth());
		assertTrue(null==game.getCurrent());
		
		Flow[] flows = games.get(1).getAllFlows();
		game = new FlowGame(flows, -1, 2);
		assertTrue(null==game.getAllFlows());
		assertEquals(0, game.getHeight());
		assertEquals(0, game.getWidth());
		assertTrue(null==game.getCurrent());
		
		game = new FlowGame(flows, 2, -1);
		assertTrue(null==game.getAllFlows());
		assertEquals(0, game.getHeight());
		assertEquals(0, game.getWidth());
		assertTrue(null==game.getCurrent());
	}
	
	@Test
	public void startFlows(){
		FlowGame game = games.get(1);
		game.startFlow(0, 0);
		assertTrue(game.getCurrent().positionMatches(0, 0));
		game.endFlow();
		assertEquals(null, game.getCurrent());
		game.startFlow(2, 0);
		assertTrue(game.getCurrent().positionMatches(2, 0));
		game.endFlow();
		game.startFlow(3, 2);
		assertTrue(game.getCurrent().positionMatches(3, 2));
		game.endFlow();
		game.startFlow(1, 1);
		assertEquals(null, game.getCurrent());
		game.startFlow(1, 3);
		assertEquals(null, game.getCurrent());
		game.startFlow(-1, 0);
		assertEquals(null, game.getCurrent());
		game.startFlow(0, -1);
		assertEquals(null, game.getCurrent());
		game.startFlow(-1, -1);
		assertEquals(null, game.getCurrent());
		game.startFlow(4, 0);
		assertEquals(null, game.getCurrent());
		game.startFlow(0, 4);
		assertEquals(null, game.getCurrent());
		game.startFlow(4, 4);
		assertEquals(null, game.getCurrent());
	}
	
	@Test
	public void startAtLastCell(){
		FlowGame game = games.get(3);
		game.startFlow(0, 0);
		game.addCell(0, 1);
		assertTrue(game.getCurrent().positionMatches(0, 1));
		game.addCell(0, 2);
		assertTrue(game.getCurrent().positionMatches(0, 2));
		game.endFlow();
		game.startFlow(0, 1);
		assertEquals(null, game.getCurrent());
		game.startFlow(0, 2);
		assertTrue(game.getCurrent().positionMatches(0, 2));
	}
	
	@Test
	public void startBeforeEnd(){
		FlowGame game = games.get(3);
		game.startFlow(0, 0);
		game.addCell(0, 1);
		game.startFlow(1, 0);
		assertTrue(game.getCurrent().positionMatches(0, 1));
		
		
	}
	
	@Test
	public void add(){
		FlowGame game = games.get(1);
		game.startFlow(1, 0);
		game.addCell(2, 0);
		assertTrue(game.getCurrent().positionMatches(1, 0));
		game.addCell(1, 1);
		assertTrue(game.getCurrent().positionMatches(1, 1));
		game.addCell(1, 2);
		assertTrue(game.getCurrent().positionMatches(1, 1));
		game.addCell(0, 2);
		assertTrue(game.getCurrent().positionMatches(1, 1));
		game.addCell(2, 1);
		assertTrue(game.getCurrent().positionMatches(2, 1));
		game.endFlow();
		
		game.startFlow(0, 0);
		game.addCell(-1, 0);
		assertTrue(game.getCurrent().positionMatches(0, 0));
		game.addCell(0, -1);
		assertTrue(game.getCurrent().positionMatches(0, 0));
		game.addCell(-1, -1);
		assertTrue(game.getCurrent().positionMatches(0, 0));
		game.endFlow();
	}
	
	@Test
	public void startOnEndpointClears(){
		FlowGame game = games.get(3);
		game.startFlow(0, 0);
		game.addCell(0, 1);
		game.addCell(0, 2);
		Flow[] flows = game.getAllFlows();
		ArrayList<Cell> cells = flows[4].getCells();
		assertTrue(cells.get(cells.size()-1).positionMatches(0, 2));
		game.endFlow();
		game.startFlow(0, 0);
		flows = game.getAllFlows();
		assertEquals(1, flows[4].getCells().size());
		
		game.startFlow(0, 0);
		game.addCell(0, 1);
		game.addCell(0, 2);
		game.endFlow();
		game.startFlow(1, 4);
		flows = game.getAllFlows();
		assertEquals(1, flows[4].getCells().size());
	}
	
	@Test
	public void getCount(){
		assertEquals(games.get(1).getCount(), 8);
		assertEquals(games.get(2).getCount(), 10);
		assertEquals(games.get(3).getCount(), 10);
		
		FlowGame game = games.get(1);
		game.startFlow(0, 0);
		game.addCell(0, 1);
		game.addCell(0, 2);
		game.endFlow();
		game.startFlow(2, 0);
		game.addCell(3, 0);
		game.endFlow();
		assertEquals(game.getCount(), 11);
		
		game = games.get(2);
		game.startFlow(0, 4);
		game.addCell(0, 3);
		game.addCell(0, 2);
		game.addCell(0, 1);
		assertEquals(game.getCount(), 13);
		game.endFlow();
		game.startFlow(0, 4);
		assertEquals(game.getCount(), 10);
		
	}
	
	@Test
	public void isComplete(){
		FlowGame game = games.get(0);
		game.startFlow(0, 0);
		game.addCell(0, 1);
		game.addCell(0, 2);
		assertFalse(game.isComplete());
		game.addCell(0, 3);
		assertTrue(game.isComplete());
	}
	
	
	
}
