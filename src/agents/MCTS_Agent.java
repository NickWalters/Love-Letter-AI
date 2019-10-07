package agents;
import loveletter.*;
import java.lang.Math;
import java.util.*;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class MCTS_Agent implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  int bigN = 0;
  


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
		  	// MCTS(bestChild(root))
		  	// may need recursion, I don't know
		  	return bestChild(root);
	   }
	  return null;
  }
  
  
  // give the parent node of the Node to traverse, and search all of its children
  private Node traverse(Node node) {
	  
	  ArrayList<Node> children = node.getChildren();
	  Node maxChild = null;
	  double maxUCB1 = -1;
	  
	  // for each child, check the UCB1 value, and select the highest value (of the leaf nodes/children)
	  for(Node child: children) {
		  double UCB1 = UCB1(child);
		  if(UCB1 >= maxUCB1) {
			  maxUCB1 = UCB1;
			  maxChild = child;
		  }
	  }
	  return maxChild;
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
  
  private double UCB1(Node node) {
	  int v = node.getT();
	  double c = 0.7;
	  int n = node.getN();
	  double UCB1 = 0;
	  
	  if((v == 0) & (n==0)) {
		  UCB1 = Double.POSITIVE_INFINITY;
	  }
	  else {
		  UCB1 = v + c*(Math.sqrt(bigN/n));
	  }	
	  return UCB1;
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
	  int t; // the total score. this is updated during back-propagation
	  int n; // the number of times this node has been visited. this is updated during back-propagation and traversal
	  ArrayList<Node> childArray;
	  
	  
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
	  
	  public void addChild(Node child) {
		  childArray.add(child);
	  }
	  
	  public ArrayList<Node> getChildren() {
		  return childArray;
	  }
	  
	  public void setT(int t) {
		  this.t = t;
	  }
	  
	  public int getT() {
		  return t;
	  }
	  public void incrementN() {
		  n++;
	  }
	  
	  public int getN(){
		  return n;
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

