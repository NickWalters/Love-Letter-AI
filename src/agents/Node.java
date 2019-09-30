package agents;
import loveletter.*;
import java.util.*;

public class Node {
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