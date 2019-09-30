package agents;
import loveletter.*;
import java.util.Random;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */

// @author Nicholas Walters
public class simpleReflex implements Agent{

  private Random rand;
  private State current;
  private int myIndex;

  
  //0 place default constructor
  public simpleReflex(){
    rand  = new Random();
  }

  
  
  /**
   * Reports the agents name
   * */
  public String toString(){return "simpleReflexAgent";}


  
  
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
	    
	    int target = rand.nextInt(current.numPlayers());
	    Card drawnCard = c;
	    Card handCard = current.getCard(myIndex);
	    while(!current.legalAction(act, c)){
	    try {
	    	switch(drawnCard) {
	    		case GUARD:
	    			// as guard is the lowest value, discard it straight away
	    			act = Action.playGuard(myIndex, target, Card.values()[rand.nextInt(7)+1]);
	    			break;
	    		case PRIEST:
	    			// select the higher value card
	    			if(compareCards(handCard, drawnCard)) {
	    				act = playHand(handCard, target);
	    				break;
	    			}else {
	    				act = Action.playPriest(myIndex, target);
	    				break;
	    			}
	    		case BARON:
	    			// select the higher value card
	    			if(compareCards(handCard, drawnCard)) {
	    				act = playHand(handCard, target);
	    				break;
	    			}else {
	    				act = Action.playBaron(myIndex, target);
	    				break;
	    			}
	    		case HANDMAID:
	    			// select the higher value card
	    			if(compareCards(handCard, drawnCard)) {
	    				act = playHand(handCard, target);
	    				break;
	    			}else {
	    				act = Action.playHandmaid(myIndex);
	    				break;
	    			}
	    		case PRINCE:
	    			// select the higher value card
	    			if(compareCards(handCard, drawnCard)) {
	    				act = playHand(handCard, target);
	    				break;
	    			}else {
	    				act = Action.playPrince(myIndex, target);
	    				break;
	    			}
	    		case KING:
	    			// select the higher value card
	    			if(compareCards(handCard, drawnCard)) {
	    				act = playHand(handCard, target);
	    				break;
	    			}else {
	    				act = Action.playKing(myIndex, target);
	    				break;
	    			}
	    		case COUNTESS:
	    			// if you have a prince, or king, or princess, then play countess
	    			if((handCard.value() > 4)) {
	    				act = Action.playCountess(myIndex);
	    				break;
	    			}else {
	    				// keep the high value countess, discard other card
	    				act = playHand(handCard, target);
	    				break;
	    			}
			default:
				break;
	    	}
	    }catch(IllegalActionException e){/*do nothing, just try again*/}
	    }
	    return act;
  }


  public Action playHand(Card handCard, int target) {
	  Action act = null;
	  try{
	        switch(handCard){
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
	  return act;
  }


// returns true if drawnCard is greater value
	public boolean compareCards(Card handCard, Card drawnCard) {
	  if(drawnCard.value() > handCard.value()) {
		  return true;
	  }else {
		  // still also works fine if handCard==drawnCard
		  return false;
	  }
	}
}


