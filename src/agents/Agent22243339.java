package agents;
import loveletter.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */

// @author 22243339

public class Agent22243339 implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  int topScorer;
  
  //0 place default constructor
  public Agent22243339(){
    rand  = new Random();
  }

  
  
  /**
   * Reports the agents name
   * */
  public String toString(){return "bayesProbabilityAgent";}


  
  
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
		  
		  
		  // check if a player has played a countess recently
		  boolean guardTargetFound = false; // only use with guard
		  int guardTarget = rand.nextInt(current.numPlayers());		
		  for(int p=0; p<current.numPlayers(); p++) {
			  if(p == myIndex) {
				  continue;
			  }
			  else {
				  if(!current.eliminated(p)) {
					  if(countessPlayed(current, p)) {
						  guardTarget = p;
						  guardTargetFound = true;
						  break;
					  }
				  }
			  }
		  }
		  
		  
		  // select the higher value card by default
		  if (alreadyDecided == false) {
			  if(handCard <= drawnCard) {
				  play = current.getCard(myIndex);
			  }else {
				  play = c;
			  }
			// if you have a baron, then playing this can be a Risk. Agent will Check if its OK
			  if(play.value() == 3) {
				  if(playBaronOK(current, c) == false) {
					  if(current.getCard(myIndex).value() == 3) {
						  play = c;
					  }
					  if(c.value() == 3) {
						  play = current.getCard(myIndex);
					  }
				  }
			  }
		  }
		  
		  
		  // FINAL STEP
		  // check if your targets are eliminated (or hand-maid), if not all eliminated then proceed with choosing target
		  if(allUnableToEliminate(current)) {
			  Card hCard = current.getCard(myIndex);
			  Card dCard = c;
			  // play the prince on yourself
			  if(hCard.value() == 5) {
				  play = hCard;
				  target = myIndex;
			  }
			  else if(dCard.value() == 5) {
				  play = dCard;
				  target = myIndex;
			  }
			  else {
				  // can't do anything
				  play = null;
				  target = getHighestPlayer(current, myIndex);
			  }
		  }
		  else {
			  target = getHighestPlayer(current, myIndex);
		  }
		  
		  
		  // TARGET and PLAY has been found, execute main statement
		  try {
			  // System.out.println("I will play a: " + play.toString());
			  switch(play) {
			        case GUARD:
			        	if(guardTargetFound) {
			        		// person has played a countess, so likely has a king or prince
			        		int[] thisDeck = unseenDeck(current, c);
			        		if(thisDeck[4] >= thisDeck[5]) {
			        			// guess prince
			        			act = Action.playGuard(myIndex, guardTarget, Card.values()[4]);
			        		}
			        		else {
			        			// guess king
			        			act = Action.playGuard(myIndex, guardTarget, Card.values()[5]);
			        		}
			        		break;
			        	}
			        	else {
			        		guardTarget = rand.nextInt(current.numPlayers());
			        		act = Action.playGuard(myIndex, target, Card.values()[guess(current, c)]);
			        		break;
			        	}
		            case PRIEST:
		            	act = Action.playPriest(myIndex, target);
		            	break;
		            case BARON:
		            	// if we have used priest before
	            		if(!knownCards.isEmpty()) {
	            			for(Integer opponent : knownCards.descendingKeySet()){
	            				if(!current.handmaid(opponent) && !current.eliminated(opponent) && opponent != myIndex){
	            					int value = knownCards.get(opponent);
	            					if(value > current.getCard(myIndex).value()) {
	            						target = opponent;
	            					}
	            				}
	            			}
	            		}
		            	act = Action.playBaron(myIndex, target);
		            	break;            
		            case HANDMAID:
		            	act = Action.playHandmaid(myIndex);
		            	break;
		            case PRINCE:
		            	if(allUnableToEliminate(current)) {
		            		act = Action.playPrince(myIndex, myIndex);
		            		break;
		            	}
		            	else {
		            		// if we have used priest before
		            		if(!knownCards.isEmpty()) {
		            			for(Integer opponent : knownCards.descendingKeySet()){
		            				if(!current.handmaid(opponent) && !current.eliminated(opponent) && opponent != myIndex){
		            					int value = knownCards.get(opponent);
		            					if(value >= 6) {
		            						target = opponent;
		            					}
		            				}
		                    	}
		            		}
		            		act = Action.playPrince(myIndex, target);
		            		break;
		            	}
		            case KING:
		            	if(!knownCards.isEmpty()) {
	            			for(Integer opponent : knownCards.descendingKeySet()){
	            				if(!current.handmaid(opponent) && !current.eliminated(opponent) && opponent != myIndex){
	            					int value = knownCards.get(opponent);
	            					if(value > 6) {
	            						target = opponent;
	            					}
	            				}
	            			}
		            	}
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
  
  
  
  
  
  

  
  
  
  
  
  /**
   * Guesses the value that the opponent may have, and gives the value/number of the card
   * @param state: the state of the game to extract information from
   * @param drawnCard the drawnCard at the start of the turn so you can subtract this number from unseen deck
   * @return int the card (value) guess of what the opponent may have
   * **/
  public int guess(State current, Card drawnCard) {
	  int[] deck = unseenDeck(current, drawnCard);
	  
	  int largestProbability = 0;
	  for (int i=0; i<8; i++)
	  {
		  // opponent players will try to keep higher value cards, find highest value & count card tradeoff
	      if ( deck[i] >= deck[largestProbability] ) largestProbability = i;
	  }
	  return largestProbability;
  }
  
  
  
  
  
  
  
  
  
  /**
   * check to determine if the target opponent has played a countess last turn
   * @param state: the state of the game to extract information from
   * @param player: the player to check if they have played the countess in the last move
   * @return true the player played the countess recently
   * **/
  public boolean countessPlayed(State current, int player) {
	  Iterator<Card> it = current.getDiscards(player);
	  
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
  
  
  
  
  
  
  
  
  /**
   * This method will get the player who has the highest current score in the game so far
   * @param state : the state of the game to extract information from
   * @param myIndex : the index of the agent
   * @return the index of the opponent
   * **/
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
  
  
  
  
  
  
  
  /**
   * Checks if all the current players are protected by hand-maid or are eliminated
   * @param state: the state of the game to extract information from
   * @return true if you are unable to eliminate any other remaining players in the round
   * **/
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
  
  
  
  
  
  
  /**
   * 
   * @param state : the state of the game to extract information from
   * @param myIndex : the index of the agent
   * @param knownCards : the cards array which are known by using the priest
   * **/
  private void fillKnownCards(State state, int myIndex, TreeMap<Integer,Integer> knownCards){
      for(int i=0;i<state.numPlayers();i++){
          if(i == myIndex) continue;
          Card card = state.getCard(i);
          if(card != null){
              knownCards.put(i,state.getCard(i).value());
          }
      }
  }
 
  
  
  
  /**
   * As playing baron is a Risky move, this method calculates the likelihood that you would lose
   * @return false if you shouldn't play the baron
   * @return true if you should play the baron
   * @param state: the state of the game to extract information from
   * **/
  private boolean playBaronOK(State current, Card c) {
	  int[] deck = unseenDeck(current, c);
	  int comparingCard = -1;
	  
	  if(current.getCard(myIndex).value() == 3) {
		  comparingCard = c.value();
	  }
	  else{
		  comparingCard = current.getCard(myIndex).value();
	  }
	  
	  int unseenCount = 0;
	  int countBelow = 0;
	  int countAbove = 0;
	  int i=0;
	  for(int cardCount: deck) {
		  // baron or less
		  if(i+1<=comparingCard) {
			  countBelow = countBelow + cardCount;
			  unseenCount = unseenCount + cardCount;
			  i++;
		  }
		  else {
			  countAbove = countAbove + cardCount;
			  unseenCount = unseenCount + cardCount;
			  i++;
		  }
	  }
	  float winProbability = (((float) countBelow) / unseenCount) * 100;
	  
	  if(winProbability > 60) {
		  return true;
	  }
	  else {
		  return false;
	  }
  }
  
  
  
  /**
   * this function gives an array indexed by each card possibility, which shows the amount of unseen cards
   * @param state: the state of the game to extract information from
   * @return int[] an array of the cards that are unseen
   * **/
  private int[] unseenDeck(State current, Card drawnCard) {
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
	  int drawnIndexDeduct = drawnCard.value()-1;
	  deck[indexOfDeduction] = deck[indexOfDeduction] - 1;
	  deck[drawnIndexDeduct] = deck[drawnIndexDeduct] - 1;
	  
	  return deck;
  }
  
}





