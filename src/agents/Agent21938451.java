package agents;
import loveletter.*;

import java.util.*;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
//@author 21938451
public class Agent21938451 implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  int[] playedCard = {0, 0, 0, 0, 0, 0, 0, 0};
  int topScorer=0;
  
  //0 place default constructor
  public Agent21938451(){
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
	if(act.card().value() == 1) {
		playedCard[0] += 1;
	}else if(act.card().value() == 2){
		playedCard[1] += 1;
	}else if(act.card().value() == 3){
		playedCard[2] += 1;
	}else if(act.card().value() == 4){
		playedCard[3] += 1;
	}else if(act.card().value() == 5){
		playedCard[4] += 1;
	}else if(act.card().value() == 6){
		playedCard[5] += 1;
	}else if(act.card().value() == 7){
		playedCard[6] += 1;
	}else if(act.card().value() == 8){
		playedCard[7] += 1;
	}
//    System.out.println("================PLAYEDCARD===========");
//    System.out.println(Arrays.toString(playedCard));
//    System.out.println("================PLAYEDCARD===========");

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
		  boolean baronTargetFound = false;
		// Baron and Princess 
		  if((handCard==3)||(drawnCard==3)) {
			  if(handCard==8 || drawnCard==8) {
				  baronTargetFound = true;
				  if(drawnCard==3) {
					  play = c;
					  alreadyDecided = true;
				  }else {
					  play = current.getCard(myIndex);
					  alreadyDecided = true;
				  }
			  }
			  
		  }
		  
		  //always played handmaid 
		  if(handCard==4 || drawnCard==4 ) {
			  if(handCard == 4) {
				  play = current.getCard(myIndex);
				  alreadyDecided = true;
			  }else {
				  play = c;
				  alreadyDecided = true;
			  }
		  }
		  
		  // this checks if you have a countess needing to be played
		  if((handCard == 7 || drawnCard == 7)) {
            // king
	        if(handCard == 6 || drawnCard == 6) {
	          if(drawnCard == 7) {
	        	  play = c;
	        	  alreadyDecided = true;
	          }else{
	        	  play = current.getCard(myIndex);
	        	  alreadyDecided = true;
	          }
	        }
            // prince
	        if(handCard == 5 || drawnCard == 5) {
	          if(drawnCard == 7) {
	        	  play = c;
	        	  alreadyDecided = true;
	          }else {
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

		  //play the lowest value card
		  if (alreadyDecided == false) {
			  if(handCard <= drawnCard) {
				  play = current.getCard(myIndex);
			  }else {
				  play = c;
			  }
		  }
		  
		  //check if player played countess for the last turn 
		  boolean targetFoundAlready = false; // only use with guard
		  int guardTarget = rand.nextInt(current.numPlayers());		
		  for(int r=0; r<current.numPlayers(); r++) {
			  if(r == myIndex) {
				  continue;
			  }else {
				  if(!current.eliminated(r)) {
					  if(countessPlayed(current, r)) {
						  guardTarget = r;
						  targetFoundAlready = true;
						  break;
					  }
				  }
			  }
		  }
		  
		  //check who is the highest scorer then target it use baron
		  int baronTarget = rand.nextInt(current.numPlayers());	
		  for(int i=0; i<current.numPlayers(); i++) {
			  if(i==myIndex) {
				  continue;
			  }else {
					  if(topScorer<current.score(i)) {
						  topScorer = current.score(i);
					  }
				  
			  }
		  }
		  for(int i=0; i<current.numPlayers(); i++) {
			  if(i==myIndex) {
				  continue;
			  }else {
				  if(current.eliminated(topScorer)) {
					  baronTarget = rand.nextInt(current.numPlayers());	
				  }
			  }
		  }
//		  System.out.println("===========================================Highest player is "+ topScorer );

		  if(allUnableToEliminate(current)) {
			  Card hCard = current.getCard(myIndex);
			  Card dCard = c;
			  // play the prince on yourself
			  if(hCard.value() == 5) {
				  play = current.getCard(myIndex);
				  target = myIndex;
			  }else if(dCard.value() == 5) {
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
		  // int target = rand.nextInt(current.numPlayers());	  
		  try {
			  // System.out.println("I will play a: " + play.toString());
			  switch(play) {
			        case GUARD:
			        	if(targetFoundAlready) {
			        		//check if all Prince has been played
			        		if(playedCard[4] !=2) {
			        		System.out.println("target found it!!!!!!!!!!!!!!" + act);
			        		// guess prince (or King)
			        		act = Action.playGuard(myIndex, guardTarget, Card.values()[4]);
			        		}else {
			        			act = Action.playGuard(myIndex, guardTarget, Card.values()[5]);
			        		}
			        		break;
			        	}
			        	else {
			        		guardTarget = rand.nextInt(current.numPlayers());
			        		act = Action.playGuard(myIndex, guardTarget, Card.values()[rand.nextInt(7)+1]);
			        		break;
			        	}
		            case PRIEST:
		            	act = Action.playPriest(myIndex, target);
		            	break;
		            case BARON:
		            	if(baronTargetFound) {
			            	act = Action.playBaron(myIndex, baronTarget);
		            	}else {
		            		act = Action.playBaron(myIndex, target);
		            	}
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
		            	act = null;
			  }
		  }catch(IllegalActionException e){} 
	  	}
	  return act;
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
	  int count = 0;
	  
	  for(int i=0; i<numPlayers; i++) {
		  if(!current.eliminated(i) || !current.handmaid(i)) {
			  count++;
		  }
	  }
	  // if all the players are eliminated or hand-maiden
	  if(count == 0) {
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