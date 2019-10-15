package agents;
import loveletter.*;
import java.util.Random;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class basicAgent implements Agent{

  private Random rand;
  private State current;
  private int myIndex;

  //0 place default constructor
  public basicAgent(){
    rand  = new Random();
  }

  /**
   * Reports the agents name
   * */
  public String toString(){return "basicAgent";}


  /**
   * Method called at the start of a round
   * @param start the starting state of the round
   **/
  public void newRound(State start){
    current = start;
    myIndex = current.getPlayerIndex();
  }

  /**
   * Method called when any agent performs an action. 
   * @param act the action an agent performs
   * @param results the state of play the agent is able to observe.
   * **/
  public void see(Action act, State results){
    current = results;
  }

  /**
   * Perform an action after drawing a card from the deck
   * @param c the card drawn from the deck
   * @return the action the agent chooses to perform
   * @throws IllegalActionException when the Action produced is not legal.
   * */
  public Action playCard(Card c){
    Action act = null;
    Card play;
    int drawnCard = c.value();
	int handCard = current.getCard(myIndex).value();
    
	  // this checks if you have a countess needing to be played
	  if((handCard == 7 || drawnCard == 7)) {
      // king
	        if(handCard == 6 || drawnCard == 6) {
	          if(drawnCard == 7) {
	        	  play = c;
	        	  alreadyDecided = true;
	          }
	          else{
	        	  play = current.getCard(myIndex);
	        	  alreadyDecided = true;
	          }
	        }
      // prince
	        if(handCard == 5 || drawnCard == 5) {
	          if(drawnCard == 7) {
	        	  play = c;
	        	  alreadyDecided = true;
	          }
	          else {
	        	  play = current.getCard(myIndex);
	        	  alreadyDecided = true;
	          }
	        }
	      }
	  
	  
	  // if you have a princess, play the other lower value card.
	  else if((handCard ==8) || (drawnCard==8)) {
		  if(handCard == 8) {
			  play = c;
			  alreadyDecided = true;
		  }
		  else {
			  play = current.getCard(myIndex);
			  alreadyDecided = true;
		  }
	  }
	
	
	// select the higher value card. Very basic rule
	  else if(handCard <= drawnCard) {
		  play = current.getCard(myIndex);
	  }else {
		  play = c;
	  }
    
    while(!current.legalAction(act, c)){
      int target = rand.nextInt(current.numPlayers());
      try{
        switch(play){
          case GUARD:
            act = Action.playGuard(myIndex, target, Card.values()[rand.nextInt(7)+1]);
            break;
          case PRIEST:
            act = Action.playPriest(myIndex, target);
            break;
          case BARON:  
            act = Action.playBaron(myIndex, target);
            break;
          case HANDMAID:
            act = Action.playHandmaid(myIndex);
            break;
          case PRINCE:  
            act = Action.playPrince(myIndex, target);
            break;
          case KING:
            act = Action.playKing(myIndex, target);
            break;
          case COUNTESS:
            act = Action.playCountess(myIndex);
            break;
          default:
            act = null;//never play princess
        }
      }catch(IllegalActionException e){/*do nothing, just try again*/}  
    }
    return act;
  }
}


