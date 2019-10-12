package agents;
import loveletter.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */

// @author 22243339

public class simpleReflex implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  int topScorer;
  
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
	  Card play = null;
	  int drawnCard = c.value();
	  int handCard = current.getCard(myIndex).value();
	  boolean alreadyDecided = false;
	  TreeMap<Integer,Integer> knownCards = new TreeMap<>();
	  fillKnownCards(current,myIndex, knownCards);
	  int target = rand.nextInt(current.numPlayers());
	  
	  while(!current.legalAction(act, c)) {
		  
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
		  if((handCard ==8) || (drawnCard==8)) {
			  if(handCard == 8) {
				  play = c;
				  alreadyDecided = true;
			  }
			  else {
				  play = current.getCard(myIndex);
				  alreadyDecided = true;
			  }
		  }
		  // select the higher value card by default
		  if (alreadyDecided == false) {
			  if(handCard <= drawnCard) {
				  play = current.getCard(myIndex);
			  }else {
				  play = c;
			  }
		  }
		  

		  // check if a player has played a countess recently
		  boolean targetFoundAlready = false; // only use with guard
		  int guardTarget = rand.nextInt(current.numPlayers());		
		  for(int r=0; r<current.numPlayers(); r++) {
			  if(r == myIndex) {
				  continue;
			  }
			  else {
				  if(!current.eliminated(r)) {
					  if(countessPlayed(current, r)) {
						  guardTarget = r;
						  targetFoundAlready = true;
						  break;
					  }
				  }
			  }
		  }
		  // target somebody, but also check if they are eliminated (or countess) first
		  if(allUnableToEliminate(current)) {
			  Card hCard = current.getCard(myIndex);
			  Card dCard = c;
			  // play the prince on yourself
			  if(hCard.value() == 5) {
				  play = current.getCard(myIndex);
				  target = myIndex;
			  }
			  else if(dCard.value() == 5) {
				  play = c;
				  target = myIndex;
			  }
			  else {
				  // can't do anything
				  play = null;
			  }
		  }
		  else {
			  target = getHighestPlayer(current, myIndex);
		  }	  
		  try {
			  // System.out.println("I will play a: " + play.toString());
			  switch(play) {
			        case GUARD:
			        	if(targetFoundAlready) {
			        		// guess prince (or King)
			        		act = Action.playGuard(myIndex, guardTarget, Card.values()[4]);
			        		break;
			        	}
			        	else {
			        		guardTarget = rand.nextInt(current.numPlayers());
			        		act = Action.playGuard(myIndex, guardTarget, Card.values()[guess(current)]);
			        		break;
			        	}
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
  
  
  
  
  
  

  
  
  
  
  // returns a guess for the guard. the guess is in the form of a Card
  public int guess(State current) {
	  Card[] unseenCards = current.unseenCards();
	  int[] deck = {0,0,0,0,0,0,0,0};
	  
	  for(Card card: unseenCards){
		  try {
			  switch(card.value()) {
			    case 1:
			    	break;
			  	//priest
			  	case 2:
			  		deck[1] = deck[1] + 1;
			  		break;
			  	//baron
			  	case 3:
			  		deck[2] = deck[2] + 1;
			  		break;
			  	//hand-maid
			  	case 4:
			  		deck[3] = deck[3] + 1;
			  		break;
			  	//prince
			  	case 5:
			  		deck[4] = deck[4] + 1;
			  		break;
			  	//king
			  	case 6:
			  		deck[5] = deck[5] + 1;
			  		break;
			  	//countess
			  	case 7:
			  		deck[6] = deck[6] + 1;
			  		break;
			  	// princess
			  	case 8:
			  		deck[7] = deck[7] + 1;
			  		break;
			  }
			  
		  }catch(Exception e){/*do nothing, just try again*/} 
	  }
	  // minus the agents current hand
	  int indexOfDeduction = current.getCard(myIndex).value()-1;
	  deck[indexOfDeduction] = deck[indexOfDeduction] - 1;
	  
	  int largest = 0;
	  for (int i=0; i<8; i++)
	  {
	      if ( deck[i] >= deck[largest] ) largest = i;
	  }
	  return largest;
  }
  
  
  
  
  // check to determine if the target opponent has played a countess last turn. returns true if yes
  public boolean countessPlayed(State current, int player) {
	  // System.out.println("countessPlayed()");
	  Iterator<Card> it = current.getDiscards(player);
	  // size of discard deck
	  
	  if(it == null) {
		  return false;
	  }

	  while(it.hasNext()) {
		  Card card = (Card)it.next();
		  int cardValue = card.value();
		  if(cardValue == 7) {
			  return true;
		  }
		  else {
			  return false;
		  }
	  }
	return false;
  }
  
  
  public int getHighestPlayer(State state, int myIndex) {
	  int players = state.numPlayers();
	  HashMap<Integer, Integer> hashMap = new HashMap<>();
	  int key = 0;
	  int count = 0;
	  
	  for(int i=0; i<players; i++) {
		  if(i != myIndex && !state.eliminated(i) && !state.handmaid(i)) {
			  count++;
			  hashMap.put(i, state.score(i));
			  key = i;
		     }
	      }
		  // protected by hand-maid, so do other statement
		  if(count == 0) {
			  return rand.nextInt(current.numPlayers());
		  }
	  
	      int maxValueInMap=(Collections.max(hashMap.values()));
	      for(Map.Entry<Integer, Integer> entry: hashMap.entrySet()){
	    	  if(entry.getValue() == maxValueInMap) {
	    		  key = entry.getKey();
	    	  }
	      }
	  return key;
  }
  
  
  public boolean allUnableToEliminate(State current) {
	  int numPlayers = current.numPlayers();
	  int canTarget = 0;
	  
	  for(int i=0; i<numPlayers; i++) {
		  if((!current.eliminated(i)) || (!current.handmaid(i))) {
			  if(i != myIndex) {
				  canTarget++;
			  }
		  }
	  }
	  // if all the players are eliminated or hand-maiden
	  if(canTarget == 0) {
		  return true;
	  }
	  else {
		  return false;
	  }
  }
  
  
  private void fillKnownCards(State state, int myIndex, TreeMap<Integer,Integer> knownCards){
      for(int i=0;i<state.numPlayers();i++){
          if(i == myIndex) continue;
          Card card = state.getCard(i);
          if(card != null){
              knownCards.put(i,state.getCard(i).value());
          }
      }
  }
  
}





