package agents;
import loveletter.*;

import java.util.*;

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
  
  
  
  
  
  public Node MCTS(Node root) {
	   int maxIterations = 9000;
	   int i=0;
	   while(i != maxIterations) {
		   Node leaf = traverse(root); // selection and expansion
		  	int simulation_result = rollout(leaf); // simulation
		  	backpropagate(leaf, simulation_result); // back-propagate
		  	i++;
		  	return bestChild(root);
	   }
	  return null;
  }
  
  
  
  private Node traverse(Node node) {
	  
	  return null;
  }
  
  
  private int rollout(Node node) {
	  return -1;
  }
  
  private Node rolloutPolicy(Node node) {
	  return null;
  }
  
  private void backpropagate(Node node, int result) {
	  
  }
  
  private Node bestChild(Node node) {
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
  
  
  
  // node data structure
  private static class Node {
	  State state;
	  Node parent;
	  List<Node> childArray;
	  
	  
	  // setters and getters
	  public State getState(){
	    return state;
	  }
	  
	  public Node getParent(){
	    return parent;
	  }
	  
	  public void setParent(Node parent){
	    this.parent = parent;
	  }
	  
	  public void setState(State state){
	    this.state = state;
	  }
	}
  
  
  
  	// graph data structure
  private static class Graph 
  { 
      int value; 
      LinkedList<Integer> adjListArray[]; 
        
      // constructor  
      Graph(int value) 
      { 
          this.value = value; 
          adjListArray = new LinkedList[value]; 
             
          for(int i = 0; i < value ; i++){ 
              adjListArray[i] = new LinkedList<>(); 
          } 
      } 
  }
  
}

