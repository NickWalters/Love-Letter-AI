package agents;

import java.util.Random;

import loveletter.Action;
import loveletter.Agent;
import loveletter.Card;
import loveletter.IllegalActionException;
import loveletter.State;

public class agent1 implements Agent{
	private Random rand;
	private State current;
	private int myIndex;

	private enum cardLeft{
		GUARD(1,"Guard",5),
	    PRIEST(2,"Priest",2),
	    BARON(3,"Baron",2),
	    HANDMAID(4,"Handmaid",2),
	    PRINCE(5,"Prince",2),
	    KING(6,"King",1),
	    COUNTESS(7,"Countess",1),
	    PRINCESS(8,"Princess",1);
	  
	    private int value; //numerical value of card
	    private String name; //String description of card
	    private int count; //nnumber of cards in the deck
	    
	    private cardLeft(int value, String name, int count){
	        this.value = value;
	        this.name = name;
	        this.count = count;
	      }
	    public int value(){return value;}
	    
	    public String toString(){return name;}

	    public int count(){return count-1;}
	}

	//0 place constructor
	  public agent1(){
	    rand  = new Random();
	  }
	  
	// player name
	public String toString() {
		return "nano";
	}

	@Override
	public void newRound(State start) {
		
		
		//initial each round
		current = start;
		myIndex = current.getPlayerIndex();
	}

	@Override
	public void see(Action act, State results) {
		
//		for(cardLeft a : cardLeft.values()) {
//			if(a. == act.card()) {
//				
//			}
//		}
		
		
		current = results;
//	    System.out.println("Player " + act.player() + " and draws " + current.getCard(myIndex) + " and played "+ act.card() );
//	    System.out.println("Player played-------------> "+act.card());
//	    System.out.println("=======Everyone can see=========");
//		System.out.println(act.card());
//		System.out.println("================================");


	}

	@Override
	public Action playCard(Card c) {
		Action act = null;
	    Card play;
	    while(!current.legalAction(act, c)){
	      if(rand.nextDouble()<0.5) play= c;
	      else play = current.getCard(myIndex);
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
