package agents;
import loveletter.*;
import java.util.Random;
// import java.util.*;
import agents.Tree;
import agents.Node;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class MCTS_Agent implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  


  public MCTS_Agent(){
	  rand  = new Random();
  }

  /**
   * Reports the agents name
   * */
  public String toString(){return "agentMCTS";}


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
  
  
  
  
  
  public void MCTS() {
	  // 1: create game environment, create agents/players
	  // 2: call playGame(agents)
	  // 3: playGame(agents) calls the methods of this class, such as newRound(), see()
	  // 4: while round is not over, call drawCard() and playCard() until round is finished.
	  
	  
	  
  }
  
  
  
  public Action findNextMove(State state) {
	  current = board;
	  myIndex = current.getPlayerIndex();
	  
	  Tree tree = new Tree();
	  Node rootNode = tree.getRoot();
	  rootNode.setState(state);
	  rootNode.setParent(myIndex);
	  
	  
	  return null;
  }
  
  
  // this is the randomAgent example
  public Action playCard(Card c){
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

