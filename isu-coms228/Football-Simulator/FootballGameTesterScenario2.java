package hw2;

public class FootballGameTesterScenario2 {
	public static void main(String[] args){
		FootballGame game = new FootballGame();
		while (game.getYardsToGoalLine() < 0){
			game.runOrPass(4);
		}
		
	}
}
