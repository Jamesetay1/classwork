package hw2;
/**
 * This class encapsulates the logic and state for a simplified
 * game of American football.  
 * 
 * @author James Taylor
 */
public class FootballGame {
	
	
//------------------------------------------------------------------//	
	  /**
	   * Number of points awarded for a touchdown.
	   */  
	  public static final int TOUCHDOWN_POINTS = 6;
	  
	  /**
	   * Number of points awarded for a successful extra point attempt
	   * after a touchdown.
	   */ 
	  public static final int EXTRA_POINTS = 1;
	  
	  /**
	   * Number of points awarded for a field goal.
	   */
	  public static final int FIELD_GOAL_POINTS = 3;
	  
	  /**
	   * Total length of the field from goal line to goal line, in yards.
	   */
	  public static final int FIELD_LENGTH = 100;
	  
	  /**
	   * Initial position of the offensive team after receiving a kickoff.
	   */ 
	  public static final int STARTING_POSITION = 70;
	  
	  /**
	   * Yards required to get a first down.
	   */
	  public static final int YARDS_FOR_FIRST_DOWN = 10;
//--------------------------------------------------------//
  /**
   * ballAt keeps track of how far the ball is away from the endzone.
   */
  private int ballAt;
  /**
   * down keeps track of the down (1-4)
   */
  private int down;
  /**
   * whichTeam has the ball and is on the offensive
   */
  private int whichTeam;
  /** 
   * score for team0 and team1
   */
  private int score0;
  private int score1;
  /**
   * "Yards to Go" (Until a first down)
   */
  private int ytg;
  /**
   * Constructs the Football Game
   * The ball is placed at "70" (so the 30 yard line in real football)
   * The down is set to 1 because it is first down!
   * whichTeam is 0 because team0 starts with the ball
   * ytg is set to YARDS_FOR_FIRST_DOWN ("10"). 1st and ten! 
   */
	  public FootballGame(){
		 ballAt = STARTING_POSITION;
		 down = 1;
		 whichTeam = 0;
		 ytg = YARDS_FOR_FIRST_DOWN;
	  }
	  /**
	   * 
	   * @param success is used to say if the extra point was good or not
	   * if it is, the point is added to the proper team. 
	   * Regardless, the ball changes possession, it is first down
	   * and the ball is put at STARTING_POSITION.
	   */
	  public void extraPoint(boolean success){
		  if (success){
			  if (whichTeam==0)
				  score0+=EXTRA_POINTS;
			  else score1+=EXTRA_POINTS;
		  }
		  whichTeam = 1 - whichTeam;
		  down=1;
		  ballAt=STARTING_POSITION;
		  
		  }

	  /**
	   * Similar to extraPoint -- 
	   * @param success says if the FG was good or not
	   * If it WAS GOOD - score is added to the correct team
	   * 	Ball changes possession, its first down at the STARTING_POSITIOn
	   * If it WAS NO GOOD - no score is added, the ball changes possession
	   * 	its first down and the ball stays where it is going the other way
	   */
	  public void fieldGoal(boolean success){
		  if (success){
			  if (whichTeam==0)
				  score0+=FIELD_GOAL_POINTS;
			  else score1+=FIELD_GOAL_POINTS;
		  whichTeam = 1 - whichTeam;
		  down=1;
		  ballAt=STARTING_POSITION;
		  }
		  if (!success){
			  whichTeam = 1 - whichTeam;
			  down=1;
			  ballAt=100-ballAt;
		  }	  
	  }
	  /**
	   * 
	   * @param whichTeam is scanned in to get the score for either team 0 or 1
	   * @return the score based on which team is requested.
	   */
	  public int getScore (int whichTeam){
		  if (whichTeam == 0)
			  return score0;
		  else return score1;
	  }
	  /**
	   * 
	   * @return down which is changed in runOrPass
	   */
	  public int getDown(){
		  return down;
	  }
	  /**
	   * whichTeam for me is my offensive team so this 
	   * @return whichTeam to say who has the ball
	   */
	  public int getOffense(){
		  return whichTeam;
	  }
	  /**
	   * ytg = "Yards to Go (For first down) so I simply
	   * @return ytg
	   */
	  public int getYardsToFirstDown(){
		  return ytg;
	  }
	  /**
	   * my ballAt keeps track of exactly how many yards to go until a TD
	   * This ignores the whole 50 yard line is the middle concept
	   * of actual football but that is what the skeleton and static
	   * variables seemed to say. So this 
	   * @return ballAt variable
	   */
	  public int getYardsToGoalLine(){
		  return ballAt;
	  }
	  /**
	   * After a punt, this is used
	   * @param yards is taken in as the length of the punt
	   * First possession is changed.
	   * Then, if the ball was punted past the goal line it is put on the goal line (FIELD_LENGTH)
	   * otherwise, the ball is set at FIELD_LENGTH-ballAt-yards
	   * [This logic takes ballAt-yards for yards to the goal line, and then flips it by 100 - that #]
	   */
	  public void punt(int yards){
		  whichTeam = 1 - whichTeam;
		 if (yards>ballAt) 
			 ballAt = FIELD_LENGTH;
		 else ballAt = FIELD_LENGTH-(ballAt-yards);
		  down = 1;
		 
	  }
	  /**
	   * 
	   * @param yards is taken in and I move the ball that many yards.
	   * I also take ytg (Yards to go for a first down) and subtract the yards the ball moved form that
	   * I add a down as well.
	   * If ytg becomes 0 or a - number, 
	   * 	it is once again 1st down and they need another 10 yards.
	   * However, if ytg is still not 0 or - and the down eclipses 4...
	   * 	the ball switches possession
	   * 	the ballAt is flipped to be going the other way
	   * 	ytg is reset to 10 and down is 1.
	   *If the ball passes 100 (the offenses goal line) it is kept at 100 since there are no safetys.
	   *if the ball passes 0 (the defense goal line) a TD is added to the proper team. 	
	   * 	
	   */
	  public void runOrPass(int yards){
		  ballAt=ballAt-yards;
		  
		  ytg= ytg - yards;
		  down+=1;
		  if (ytg <= 0){
			  down = 1;
		  	 ytg=YARDS_FOR_FIRST_DOWN;
		  }
		  if (ytg > 0 && down>4){
			  whichTeam = 1 - whichTeam;
		  	  ballAt=100-ballAt;
		  	  ytg=YARDS_FOR_FIRST_DOWN;
		  	  down=1;
		  }
		  
		  if (ballAt > 100)
			  ballAt = 100;
		  
		  if (ballAt < 0){
			  if (whichTeam == 0)
				  score0+=TOUCHDOWN_POINTS;
			  else score1+=TOUCHDOWN_POINTS;
		  }
	  }
	  
	}


