package agents;

import loveletter.*;

import java.util.Random;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;






/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class Agent_22232803 implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  private int round;

  Queue<Integer> oppHand = new LinkedList<>();
  

  //0 place default constructor
  public Agent_22232803(){
    rand  = new Random();
  }

  /**
   * Reports the agents name
   * */
  public String toString(){return "Pedro";}


  /**
   * Method called at the start of a round
   * @param start the starting state of the round
   **/
  public void newRound(State start){
    current = start;
    round++;
    oppHand.clear();
    myIndex = current.getPlayerIndex(); //gets the player index
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
   * Method called when you want to target top scoring opponents
   * @param current the state of the game
   * @return topPlayer the top player of the game, or just a random player if there is no top player.
   * **/
  public int highest_score(State current){
    ArrayList<Integer> players = legalTargets(); 
    int topPlayerScore = 0;
    int topPlayer = 0;

    for(int i = 0; i < players.size(); i++) {
      if(current.score(i) >= topPlayerScore && current.score(i) >=2) {
        topPlayerScore = current.score(i);
        topPlayer = players.get(i);
      }
    }
    if(current.score(topPlayer) < 2 || topPlayer == myIndex){
      if(players.size() == 0) {
        return 0;
      }
      else return players.get(rand.nextInt(players.size()));
    }
    return topPlayer;
  }

  /**
   * Method to return an opponent's hand, if we remember it. 
   * Else target ourself, if the opponents are protected.
   * Else randomly attack.
   * @param c drawn card
   * @return results the array of possible target.
   * **/
  public KnownHand knowHand(Card c){
    //Check the queue of remembered opponents' cards
    for(int i = 0; i <= oppHand.size(); i++) {
      int target = oppHand.poll();
      Card oppCard = current.getCard(target);

      //Check if the cards are have been used yet.
      if(!current.eliminated(target) && oppCard != null){
        Card discardCard = current.getDiscards(target).next();
        if(!oppCard.equals(discardCard)){
          KnownHand oppHand = new KnownHand(target, oppCard);
          return oppHand;
        }
      }
    }
    
    //If we don't remember cards, check for legal targets
    ArrayList<Integer> targets = legalTargets();

    //If targets is empty, then opponents are protected by handmaid, target yourself 
    if(targets.isEmpty()) {
      KnownHand oppHand = new KnownHand(0, Card.values()[cardCount(c)-1]);
      return oppHand;
    }

    //If there are targets, randomly target them. 
    KnownHand oppHand = new KnownHand(targets.get(rand.nextInt(targets.size())), Card.values()[cardCount(c)-1]);

    return oppHand;
  }


  /**
   * Method to return an array list of targets that are not protected by the handmaiden. 
   * @return results the array of possible target.
   * **/
public ArrayList<Integer> legalTargets() {
  ArrayList<Integer> legalTargets = new ArrayList<Integer>();
  for(int i = 0; i < 4; i++) {
    if(!current.eliminated(i) && i != myIndex && !current.handmaid(i)){
      legalTargets.add(i);
    }
  }
  return legalTargets;
}

//Implement it so you can randomly guess the highest card amongst a pile of cards
  /**
   * Method to count all of the unseen cards in the game. 
   * @param c the drawn card
   * @return highestChanceCard the card that has the highest chance of being played.
   * **/
  public int cardCount(Card c) {
    int highestProability = 0;
    int highestChanceCard = 0;
    int[] cards = {0,0,0,0,0,0,0,0,0};
    Card[] unseenCards = current.unseenCards();

    for(int i = 0; i < unseenCards.length; i++) {
      int cardValue = unseenCards[i].value();
      //Guard 
      if(cardValue == 1){
        cards[1] += 1; 
      }
      //Priest 
      if(cardValue == 2){
        cards[2] += 1; 
      }
      //Barons 
      if(cardValue == 3){
        cards[3] += 1; 
      }
      //Handmaids 
      if(cardValue == 4){
        cards[4] += 1; 
      }
      //Prince 
      if(cardValue == 5){
        cards[5] += 1; 
      }
      //King
      if(cardValue == 6){
        cards[6] += 1; 
      }
      //Countess 
      if(cardValue == 7){
        cards[7] += 1; 
      }
      //Princess
      if(cardValue == 8){
        cards[8] += 1; 
      }

      //Take into consideration the AI's hand
      cards[current.getCard(myIndex).value()] -= 1;
      cards[c.value()] -= 1;

      //Guess for the lower level cards at the early rounds
      if(round <= 2) {
        for(int j = 8; j > 1; j--) {
          if(highestProability <= cards[j]) {
            highestProability = cards[j];
            highestChanceCard = j;
          }
        }
      }
      //Guess for the higher level cards after 2 rounds
      else {
        for(int j = 0; j < 9; j++) {
          if(highestProability <= cards[j]) {
            highestProability = cards[j];
            highestChanceCard = j;
          }
        }
      }
    }
    return highestChanceCard;
  }


  /**
   * Method called when you need to change the used card. 
   * @param drawn the drawn card
   * @param play the card that you were going to play
   * @return other card in hand.
   * **/
  public Card swapCards(Card drawn, Card play) {
    Card hand = current.getCard(myIndex);
    Card drawnCard = drawn;

    //Check if the card in hand is the played card
    if(hand.equals(play)){
      return drawnCard;
    }
    //Check if the card played is the drawn card
    if(drawnCard.equals(play)){
      return hand;
    }
    return play;
  } 
  
  /**
   * Method called when you check which card to play and return it. 
   * @param drawn the drawn card
   * @return the card to be played.
   * **/
  public Card checkCards(Card drawn){
    Card cardHand = current.getCard(myIndex);
    boolean playCard = false;

      //If you have the countess or draw the countess, check other cards
      if((cardHand == Card.COUNTESS || drawn == Card.COUNTESS)) {
        if(cardHand.value() > 4 && drawn.value() > 4) {
          playCard = true;
          if(cardHand == Card.COUNTESS) return cardHand;
          else return drawn;
        }
      }

      //Play handmaid if you get her
      if((cardHand == Card.HANDMAID || drawn == Card.HANDMAID) && playCard == false){
        playCard = true;
        if(drawn == Card.HANDMAID) return drawn;
        else return cardHand;
      }

      //Else play the lower value card
      else if(playCard == false) {
        if(cardHand.value() > drawn.value()) return drawn; //If held card value is larger than drawn card
        else return cardHand;
      }

    return drawn;
  }

  /**
   * Perform an action after drawing a card from the deck
   * @param c the card drawn from the deck
   * @return the action the agent chooses to perform
   * */
  public Action playCard(Card c){
    Action act = null;
    Card play = null;

    while(!current.legalAction(act, c)){
      play = checkCards(c);


      int target = highest_score(current);
      //Check if you are targetting yourself, as opponents are protected by handmaiden
      if(target == 0) {
        //If you have Prince, play prince on yourself 
        if((c == Card.PRINCE || current.getCard(myIndex) == Card.PRINCE) && (current.getCard(myIndex) != Card.COUNTESS || c != Card.COUNTESS)) {
          if(c == Card.PRINCE) play = c;
          else play = current.getCard(myIndex);
        }
        //If opponents are protected by handmaiden, then just target them
        else {
          for(int i = 0; i < 4; i++) {
            if(i != myIndex && !current.eliminated(i)) {
              target = i;
              break;
            }
          }
        }
      }

      try{
        switch(play){
          case GUARD:
            //Check if we remember any opponents cards
            if (oppHand.size() >= 1) {
              KnownHand oppHand = knowHand(c);
              target = oppHand.getPlayerIndex();
              Card guess = oppHand.getCard();
              act = Action.playGuard(myIndex, target, guess);
            }
            act = Action.playGuard(myIndex, target, Card.values()[cardCount(c)-1]);
            break;
          
            case PRIEST:
            act = Action.playPriest(myIndex, target);
            oppHand.add(target);
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



  public class KnownHand {
    private Card card;
    private int playerIndex;

    public KnownHand(int player, Card card){
      this.card = card;
      this.playerIndex = player;
    }

    public Card getCard() {
      return card;
    }
  
    public int getPlayerIndex(){
      return playerIndex;
    }
    
  }
  
}




